package br.pucpr.authserver.products

import br.pucpr.authserver.errors.BadRequestException
import br.pucpr.authserver.errors.NotFoundException
import br.pucpr.authserver.orders.OrderService
import br.pucpr.authserver.roles.Role
import br.pucpr.authserver.users.SortDir
import br.pucpr.authserver.users.User
import br.pucpr.authserver.users.UserService
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProductService(
    val repository: ProductRepository
) {
    fun insert(product: Product) =
        repository.save(product)

    fun findAll(): List<Product> =
        repository.findAll(Sort.by("name"))

    fun findByIdOrNull(id: Long): Product? =
        repository.findByIdOrNull(id)

    fun delete(id: Long): Product? {
        val product = repository.findByIdOrNull(id)
            ?: return null

        repository.delete(product)
        log.info("Product {} deleted!", product.name)
        return product
    }

    fun list(sortDir: SortDir, product: String?): List<Product> {
        return when (sortDir) {
            SortDir.ASC -> repository.findAll()
            SortDir.DESC -> repository.findAll(Sort.by("id").reverse())
        }
    }

    fun update(id: Long, name: String, price: Double): Product? {
        val product = repository.findByIdOrNull(id)
            ?: throw NotFoundException("Produto ${id} não encontrado!")

        if (product.name == name && product.price == price)
            return null

        product.name = name
        product.price = price
        log.info("Product {} updated!", product.name)
        return repository.save(product)
    }

    companion object {
        private val log = LoggerFactory.getLogger(ProductService::class.java)
    }
}
