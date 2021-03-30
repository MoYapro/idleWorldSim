package de.moyapro.idleworldsim.domain.valueObjects

data class Resource(val resourceType: ResourceType, val amount: Double = 1.0) : Comparable<Resource> {
    internal constructor(resourceType: ResourceType, amount: Int) : this(resourceType, amount.toDouble())

    init {
        require(0 <= amount) { NEGATIVE_AMOUNT_ERRORMESSAGE }
    }

    companion object {
        const val DIFFERENT_TYPE_ERRORMESSAGE = "Cannot compare resources of different type."
        const val NEGATIVE_AMOUNT_ERRORMESSAGE = "Amount must not be negative"
    }

    operator fun times(factor: Double) = Resource(resourceType, amount * factor)
    operator fun times(population: Population) = Resource(resourceType, amount * population.populationSize)
    override operator fun compareTo(other: Resource): Int {
        require(this.resourceType == other.resourceType) { DIFFERENT_TYPE_ERRORMESSAGE }
        return this.amount.compareTo(other.amount)
    }

    operator fun minus(other: Resource): Resource {
        require(this.resourceType == other.resourceType) { DIFFERENT_TYPE_ERRORMESSAGE }
        val newAmount = this.amount - other.amount
        check(0 <= newAmount) { NEGATIVE_AMOUNT_ERRORMESSAGE }
        return Resource(this.resourceType, newAmount)
    }

    operator fun div(quotient: Int) = this.amount / quotient
    operator fun div(other: Resource): Double {
        require(this.resourceType == other.resourceType) { DIFFERENT_TYPE_ERRORMESSAGE }
        require(0 <= other.amount) { NEGATIVE_AMOUNT_ERRORMESSAGE }
        return this.amount / other.amount
    }

    override fun toString(): String {
        return "Resource[$resourceType=$amount]"
    }

}