package br.pucpr.authserver.products

import br.pucpr.authserver.errors.BadRequestException
import br.pucpr.authserver.errors.ForbiddenException
import br.pucpr.authserver.products.requests.CreateProductRequest
import br.pucpr.authserver.products.requests.UpdateProductRequest
import br.pucpr.authserver.security.UserToken
import br.pucpr.authserver.users.SortDir
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import jakarta.validation.Valid
import org.springframework.http.HttpStatus.CREATED
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products")
class ProductController(
    val service: ProductService
) {
    @PostMapping
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("hasRole('ADMIN')")
    fun insert(
        @RequestBody @Valid product: CreateProductRequest
    ) = service.insert(product.toProduct())
        .let { ProductResponse(it) }
        .let { ResponseEntity.status(CREATED).body(it) }

    @GetMapping
    fun list(
        @RequestParam(required = false) sortDir: String?,
        @RequestParam(required = false) product: String?
    ) =
        service.list(
            sortDir = SortDir.getByName(sortDir) ?:
            throw BadRequestException("Invalid sort dir!"),
            product = product
        )
        .map { ProductResponse(it) }
        .let { ResponseEntity.ok(it) }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) =
        service.findByIdOrNull(id)
            ?.let { ProductResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("hasRole('ADMIN')")
    fun delete(@PathVariable id: Long): ResponseEntity<Void> =
        service.delete(id)
            ?.let {ResponseEntity.ok().build() }
            ?: ResponseEntity.notFound().build()

    @PatchMapping("/{id}")
    @SecurityRequirement(name = "AuthServer")
    @PreAuthorize("hasRole('ADMIN')")
    fun update(
        @PathVariable id: Long,
        @RequestBody @Valid updateProductRequest: UpdateProductRequest,
        auth: Authentication
    ): ResponseEntity<ProductResponse> {
        val token = auth.principal as? UserToken ?: throw ForbiddenException()
        if (token.id != id && !token.isAdmin) throw ForbiddenException()

        return service.update(id, updateProductRequest.name!!, updateProductRequest.price!!)
            ?.let { ProductResponse(it) }
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.noContent().build()
    }
}
