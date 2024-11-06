package br.pucpr.authserver.orders

import br.pucpr.authserver.errors.BadRequestException
import br.pucpr.authserver.errors.NotFoundException
import br.pucpr.authserver.products.Product
import br.pucpr.authserver.products.ProductRepository
import br.pucpr.authserver.products.ProductService
import br.pucpr.authserver.users.SortDir
import br.pucpr.authserver.users.UserService
import io.jsonwebtoken.Jwt
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class OrderService(val orderRepository: OrderRepository,
                   val productRepository: ProductRepository ) {

    fun createOrder(order: Order): Order {
        return orderRepository.save(order)
    }

    fun delete(id: Long): Order? {
        val order = orderRepository.findByIdOrNull(id)
            ?: return null

        orderRepository.delete(order)
        log.info("Order {} deleted!", order.id)
        return order
    }

    fun getOrderById(id: Long): Order? {
        return orderRepository.findById(id).orElse(null)
    }

    fun getAllOrders(): List<Order> {
        return orderRepository.findAll()
    }
    fun findByIdOrNull(id: Long) = orderRepository.findByIdOrNull(id)

    fun addProduct(orderId: Long, productName: String): Boolean {
        val order = orderRepository.findByIdOrNull(orderId)
            ?: throw NotFoundException("Order not found")

        val product = productRepository.findByName(productName)
            ?: throw BadRequestException("Invalid product name!")

        if (order.products.any { it.name == productName }) return false

        order.products.add(product)
        orderRepository.save(order)
        log.info("Product {} was added to order {}", product.name, order.id)
        return true
    }


    fun removeProduct(id: Long, productName: String): Boolean {
        val order = orderRepository.findByIdOrNull(id)
            ?: throw NotFoundException("Order not found")

        val product = productRepository.findByName(productName)
            ?: throw BadRequestException("Invalid product name!")

        if (!order.products.contains(product)) {
            throw BadRequestException("Product not found in the order!")
        }

        order.products.remove(product)
        orderRepository.save(order)
        log.info("Product {} was removed from order {}", product.name, order.id)
        return true
    }

    companion object {
        private val log = LoggerFactory.getLogger(OrderService::class.java)
    }
}
