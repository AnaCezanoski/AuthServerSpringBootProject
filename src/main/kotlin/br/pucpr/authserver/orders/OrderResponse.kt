package br.pucpr.authserver.orders

import br.pucpr.authserver.products.Product
import br.pucpr.authserver.products.ProductResponse
import java.time.LocalDateTime

data class OrderResponse(
    val id: Long,
    val customerName: String,
    val products: List<ProductResponse>,
    val orderDate: LocalDateTime
) {
    constructor(order: Order) : this(
        id = order.id,
        customerName = order.customerName,
        products = order.products.map { ProductResponse(it.id, it.name, it.price) },
        orderDate = order.orderDate
    )
}

