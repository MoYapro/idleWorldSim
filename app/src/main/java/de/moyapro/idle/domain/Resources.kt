package de.moyapro.idle.domain

data class Resources(var amount: Double = 0.0) {
    fun add(otherResource: Resources): Resources {
        this.amount += otherResource.amount
        return this
    }
}