//package br.pucpr.authserver.orders
//
//import br.pucpr.authserver.errors.BadRequestException
//import br.pucpr.authserver.errors.NotFoundException
//import br.pucpr.authserver.products.Product
//import br.pucpr.authserver.products.ProductRepository
//import io.kotest.matchers.shouldBe
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.verify
//import org.junit.jupiter.api.Test
//import org.junit.jupiter.api.assertThrows
//import org.springframework.data.repository.findByIdOrNull
//import java.util.Optional
//
//class OrderServiceTest {
//
//    private val orderRepository = mockk<OrderRepository>(relaxed = true)
//    private val productRepository = mockk<ProductRepository>()
//    private val orderService = OrderService(orderRepository, productRepository)
//
//    @Test
//    fun `createOrder should save and return the order`() {
//        val order = Order(id = 1, products = mutableListOf())
//        every { orderRepository.save(order) } returns order
//
//        val result = orderService.createOrder(order)
//        result shouldBe order
//
//        verify { orderRepository.save(order) }
//    }
//
//    @Test
//    fun `delete should remove the order and return it if it exists`() {
//        val orderId = 1L
//        val order = Order(id = orderId, products = mutableListOf())
//        every { orderRepository.findByIdOrNull(orderId) } returns order
//
//        val result = orderService.delete(orderId)
//        result shouldBe order
//
//        verify { orderRepository.delete(order) }
//    }
//
//    @Test
//    fun `delete should return null if the order does not exist`() {
//        val orderId = 1L
//        every { orderRepository.findByIdOrNull(orderId) } returns null
//
//        val result = orderService.delete(orderId)
//        result shouldBe null
//    }
//
//    @Test
//    fun `getOrderById should return the order if it exists`() {
//        val orderId = 1L
//        val order = Order(id = orderId, products = mutableListOf())
//        every { orderRepository.findById(orderId) } returns Optional.of(order)
//
//        val result = orderService.getOrderById(orderId)
//        result shouldBe order
//    }
//
//    @Test
//    fun `getOrderById should return null if the order does not exist`() {
//        val orderId = 1L
//        every { orderRepository.findById(orderId) } returns Optional.empty()
//
//        val result = orderService.getOrderById(orderId)
//        result shouldBe null
//    }
//
//    @Test
//    fun `getAllOrders should return a list of all orders`() {
//        val orders = listOf(Order(id = 1), Order(id = 2))
//        every { orderRepository.findAll() } returns orders
//
//        val result = orderService.getAllOrders()
//        result shouldBe orders
//    }
//
//    @Test
//    fun `addProduct should add product to order if not already present`() {
//        val orderId = 1L
//        val productName = "Sample Product"
//        val price = 5.99
//        val order = Order(id = orderId, products = mutableListOf())
//        val product = Product(id = 2, name = productName, price = price)
//
//        every { orderRepository.findByIdOrNull(orderId) } returns order
//        every { productRepository.findByName(productName) } returns product
//        every { orderRepository.save(order) } returns order
//
//        val result = orderService.addProduct(orderId, productName)
//        result shouldBe true
//        order.products shouldBe listOf(product)
//
//        verify { orderRepository.save(order) }
//    }
//
//    @Test
//    fun `addProduct should throw NotFoundException if order is not found`() {
//        val orderId = 1L
//        val productName = "Sample Product"
//        every { orderRepository.findByIdOrNull(orderId) } returns null
//
//        assertThrows<NotFoundException> {
//            orderService.addProduct(orderId, productName)
//        }
//    }
//
//    @Test
//    fun `addProduct should throw BadRequestException if product is not found`() {
//        val orderId = 1L
//        val productName = "Nonexistent Product"
//        val order = Order(id = orderId, products = mutableListOf())
//
//        every { orderRepository.findByIdOrNull(orderId) } returns order
//        every { productRepository.findByName(productName) } returns null
//
//        assertThrows<BadRequestException> {
//            orderService.addProduct(orderId, productName)
//        }
//    }
//
//    @Test
//    fun `removeProduct should remove product from order if it exists`() {
//        val orderId = 1L
//        val productName = "Sample Product"
//        val product = Product(id = 2, name = productName)
//        val order = Order(id = orderId, products = mutableListOf(product))
//
//        every { orderRepository.findByIdOrNull(orderId) } returns order
//        every { productRepository.findByName(productName) } returns product
//        every { orderRepository.save(order) } returns order
//
//        val result = orderService.removeProduct(orderId, productName)
//        result shouldBe true
//        order.products shouldBe emptyList<Product>()
//
//        verify { orderRepository.save(order) }
//    }
//
//    @Test
//    fun `removeProduct should throw NotFoundException if order is not found`() {
//        val orderId = 1L
//        val productName = "Sample Product"
//        every { orderRepository.findByIdOrNull(orderId) } returns null
//
//        assertThrows<NotFoundException> {
//            orderService.removeProduct(orderId, productName)
//        }
//    }
//
//    @Test
//    fun `removeProduct should throw BadRequestException if product is not found`() {
//        val orderId = 1L
//        val productName = "Sample Product"
//        val order = Order(id = orderId, products = mutableListOf())
//        every { orderRepository.findByIdOrNull(orderId) } returns order
//        every { productRepository.findByName(productName) } returns null
//
//        assertThrows<BadRequestException> {
//            orderService.removeProduct(orderId, productName)
//        }
//    }
//
//    @Test
//    fun `removeProduct should throw BadRequestException if product is not in the order`() {
//        val orderId = 1L
//        val productName = "Nonexistent Product"
//        val price = 11.95
//        val product = Product(id = 2, name = productName, price = price)
//        val order = Order(id = orderId, products = mutableListOf())
//
//        every { orderRepository.findByIdOrNull(orderId) } returns order
//        every { productRepository.findByName(productName) } returns product
//
//        assertThrows<BadRequestException> {
//            orderService.removeProduct(orderId, productName)
//        }
//    }
//}
