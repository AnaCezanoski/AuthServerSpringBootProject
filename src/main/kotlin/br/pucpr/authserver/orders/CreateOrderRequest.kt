package br.pucpr.authserver.orders

import br.pucpr.authserver.products.Product
import br.pucpr.authserver.products.ProductRepository

data class CreateOrderRequest(
    val customerName: String,
    val products: List<Long>
) {
    fun toOrder(productRepository: ProductRepository): Order {
        val productEntities: List<Product> = productRepository.findAllById(products)

        return Order(
            customerName = customerName,
            products = productEntities.toMutableList()
        )
    }
}

