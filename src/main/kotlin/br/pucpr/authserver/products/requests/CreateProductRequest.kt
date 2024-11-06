package br.pucpr.authserver.products.requests

import br.pucpr.authserver.products.Product
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

class CreateProductRequest (
    @field:NotBlank
    val name: String?,

    @field:NotNull
    @Min(0)
    val price: Double?
    ) {
    fun toProduct() = Product(
        name = name!!,
        price = price!!
    )
}
