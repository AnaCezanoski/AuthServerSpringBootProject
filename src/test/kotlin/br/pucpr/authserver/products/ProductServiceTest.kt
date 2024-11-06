//package br.pucpr.productservice
//
//import br.pucpr.authserver.errors.NotFoundException
//import br.pucpr.authserver.products.Product
//import br.pucpr.authserver.products.ProductRepository
//import br.pucpr.authserver.products.ProductService
//import io.kotest.matchers.shouldBe
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertThrows
//
//class ProductServiceTest {
//    private val productRepository = mockk<ProductRepository>()
//    private val productService = ProductService(productRepository)
//
//    @Test
//    fun `addProduct should save and return a new product`() {
//        val newProduct = Product(name = "Sample Product", price = 100.0)
//        val savedProduct = newProduct.copy(id = 1)
//
//        every { productRepository.save(newProduct) } returns savedProduct
//
//        val result = productService.addProduct(newProduct)
//        result shouldBe savedProduct
//
//        verify { productRepository.save(newProduct) }
//    }
//
//    @Test
//    fun `findProductById should return a product if found`() {
//        val productId = 1L
//        val product = Product(id = productId, name = "Sample Product", price = 100.0)
//
//        every { productRepository.findById(productId) } returns product
//
//        val result = productService.findByIdOrNull(productId)
//        result shouldBe product
//
//        verify { productRepository.findById(productId) }
//    }
//
//    @Test
//    fun `findProductById should throw NotFoundException if product does not exist`() {
//        val productId = 1L
//        every { productRepository.findById(productId) } returns null
//
//        assertThrows<NotFoundException> {
//            productService.findByIdOrNull(productId)
//        }
//
//        verify { productRepository.findById(productId) }
//    }
//
//    @Test
//    fun `updateProduct should update and return the product`() {
//        val productId = 1L
//        val productToUpdate = Product(id = productId, name = "Old Product", price = 80.0)
//        val updatedProduct = productToUpdate.copy(name = "Updated Product", price = 120.0)
//
//        every { productRepository.findById(productId) } returns productToUpdate
//        every { productRepository.save(updatedProduct) } returns updatedProduct
//
//        val result = productService.updateProduct(productId, updatedProduct)
//        result shouldBe updatedProduct
//
//        verify { productRepository.save(updatedProduct) }
//    }
//}
