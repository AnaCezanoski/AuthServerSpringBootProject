package br.pucpr.authserver.products

import br.pucpr.authserver.products.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository: JpaRepository<Product, Long> {
    fun findByName(name: String): Product?
    //fun findByProduct(product: String): List<Product>
}
