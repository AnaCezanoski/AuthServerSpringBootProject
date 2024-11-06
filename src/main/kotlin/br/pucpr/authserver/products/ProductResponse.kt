package br.pucpr.authserver.products

data class ProductResponse(
    val id: Long,
    val name: String,
    val price: Double)
{
    constructor(product: Product): this(
        id = product.id,
        name = product.name,
        price = product.price
    )
}
