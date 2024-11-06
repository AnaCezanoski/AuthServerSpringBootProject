package br.pucpr.authserver.products

import jakarta.persistence.*

@Entity
data class Product(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0L,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    var price: Double
)
