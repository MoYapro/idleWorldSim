package de.moyapro.idleworldsim.domain.consumption

class FoodChain {

    private val elements: MutableList<FoodChainNode> = mutableListOf()
    private val consumersWithoutProducers: MutableList<ResourceConsumer> = mutableListOf()

    operator fun get(producer: ResourceProducer): List<ResourceConsumer> {
        return elements.find { it.producer == producer }?.consumer ?: emptyList()
    }

    fun add(poc: PorC): FoodChain {
        add(poc as ResourceProducer)
        add(poc as ResourceConsumer)
        return this
    }

    fun add(producer: ResourceProducer): FoodChain {
        elements += FoodChainNode(producer)
        addConsumersWithoutFoodsource(producer)
        return this
    }

    fun producers(): Int {
        return elements.size
    }

    fun add(consumer: ResourceConsumer): FoodChain {
        val foodSources = getPossibleFoodSources(consumer)
        if (foodSources.isNotEmpty()) {
            addConsumer(foodSources, consumer)
        } else {
            consumersWithoutProducers += consumer
        }
        return this
    }

    private fun addConsumer(foodSources: List<FoodChainNode>, consumer: ResourceConsumer) {
        foodSources.forEach { it.add(consumer) }
    }

    private fun getPossibleFoodSources(consumer: ResourceConsumer) = elements
        .filter { consumer.canConsume(it.producer) }

    private fun addConsumersWithoutFoodsource(producer: ResourceProducer) {
        consumersWithoutProducers.filter { it.canConsume(producer) }.forEach { add(it) }
        consumersWithoutProducers.removeIf { it.canConsume(producer) }
    }


}

private data class FoodChainNode(val producer: ResourceProducer) {
    val consumer: MutableList<ResourceConsumer> = mutableListOf()
    fun add(newConsumer: ResourceConsumer): FoodChainNode {
        consumer += newConsumer
        return this
    }


}
