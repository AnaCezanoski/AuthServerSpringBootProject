package br.pucpr.authserver.orders

import br.pucpr.authserver.products.Product
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
data class Order(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val customerName: String,

    @Column(nullable = false)
    val orderDate: LocalDateTime = LocalDateTime.now(),

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "order_product",
        joinColumns = [JoinColumn(name = "order_id")],
        inverseJoinColumns = [JoinColumn(name = "product_id")]
    )
    val products: MutableList<Product> = mutableListOf()
)
