package br.pucpr.authserver.products.requests

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UpdateProductRequest (
    @field:NotBlank
    val name: String?,

    @field:NotNull
    @Min(0)
    val price: Double?
)