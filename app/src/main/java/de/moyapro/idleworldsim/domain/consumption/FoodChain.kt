package de.moyapro.idleworldsim.domain.consumption

class FoodChain {

    private val elements: MutableList<FoodChainNode> = mutableListOf()
    private val consumersWithoutProducers: MutableList<ResourceConsumer> = mutableListOf()

    fun add(producer: ResourceProducer): FoodChain {
        elements.add(FoodChainNode(producer))
        consumersWithoutProducers.filter { it.canConsume(producer) }.forEach { add(it) }
        consumersWithoutProducers.removeIf { it.canConsume(producer) }

        return this
    }

    fun nodeSize(): Int {
        return elements.size
    }

    fun add(consumer: ResourceConsumer): FoodChain {
        val foodSources = elements
            .filter { consumer.canConsume(it.producer) }
        if (foodSources.isNotEmpty()) {
            foodSources.forEach { it.add(consumer) }
        } else {
            consumersWithoutProducers += consumer
        }
        return this
    }

    fun add(poc: PorC): FoodChain {
        add(poc as ResourceProducer)
        add(poc as ResourceConsumer)
        return this
    }

    operator fun get(producer: ResourceProducer): List<ResourceConsumer> {
        return elements.find { it.producer == producer }?.consumer ?: emptyList()
    }


}

private data class FoodChainNode(val producer: ResourceProducer) {
    val consumer: MutableList<ResourceConsumer> = mutableListOf()
    fun add(newConsumer: ResourceConsumer): FoodChainNode {
        consumer += newConsumer
        return this
    }


}
