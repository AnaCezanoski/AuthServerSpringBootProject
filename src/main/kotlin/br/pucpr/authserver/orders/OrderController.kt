package br.pucpr.authserver.orders

import br.pucpr.authserver.errors.ForbiddenException
import br.pucpr.authserver.products.ProductRepository
import br.pucpr.authserver.security.UserToken
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/orders")
class OrderController(val orderService: OrderService,
                      val productRepository: ProductRepository
) {

    @PostMapping
    fun createOrder(@RequestBody @Valid order: CreateOrderRequest): ResponseEntity<OrderResponse> {
        val orderToCreate = order.toOrder(productRepository)
        val createdOrder = orderService.createOrder(orderToCreate)
        val orderResponse = OrderResponse(createdOrder)
        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse)
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("hasRole('ADMIN')")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> {
        return orderService.delete(id)
            ?.let { ResponseEntity.ok().build() }
            ?: ResponseEntity.notFound().build()
    }

    @SecurityRequirement(name = "AuthServer")
    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    fun findById(@PathVariable id: Long,
                 auth: Authentication): ResponseEntity<OrderResponse> {
        val token = auth.principal as? UserToken ?: throw ForbiddenException()
        if (token.id != id && !token.isAdmin) throw ForbiddenException()

        val order = orderService.findByIdOrNull(id)
        return if (order != null) {
            ResponseEntity.ok(OrderResponse(order))
        } else {
            ResponseEntity.notFound().build()
        }
    }

    @GetMapping
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("hasRole('ADMIN')")
    fun getAllOrders(): ResponseEntity<List<OrderResponse>> {
        val orders = orderService.getAllOrders()
        return ResponseEntity.ok(orders.map { OrderResponse(it) })
    }

    @SecurityRequirement(name = "AuthServer")
    @PutMapping("/{id}/products/{product}")
    @PreAuthorize("permitAll()")
    fun addProduct(@PathVariable id: Long, @PathVariable product: String, auth: Authentication): ResponseEntity<Void> {
        val token = auth.principal as? UserToken ?: throw ForbiddenException()
        if (token.id != id && !token.isAdmin) throw ForbiddenException()

        return if (orderService.addProduct(id, product)) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.status(HttpStatus.NO_CONTENT).build()
        }
    }

    @SecurityRequirement(name = "AuthServer")
    @DeleteMapping("/{id}/products/{product}")
    @PreAuthorize("permitAll()")
    fun removeProduct(@PathVariable id: Long, @PathVariable product: String, auth: Authentication): ResponseEntity<Void> {
        val token = auth.principal as? UserToken ?: throw ForbiddenException()
        if (token.id != id && !token.isAdmin) throw ForbiddenException()

        return if (orderService.removeProduct(id, product)) {
            ResponseEntity.ok().build()
        } else {
            ResponseEntity.status(HttpStatus.NO_CONTENT).build()
        }
    }
}

