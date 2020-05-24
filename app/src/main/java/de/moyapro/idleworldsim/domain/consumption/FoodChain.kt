package de.moyapro.idleworldsim.domain.consumption

import java.lang.StringBuilder

/**
 * Implement the food chain of a given set of producers and consumers.
 */
class FoodChain {

    private val elements: MutableList<FoodChainNode> = mutableListOf()
    private val consumersWithoutProducers: MutableList<ResourceConsumer> = mutableListOf()

    /**
     * Get all consumers of the given producer
     */
    operator fun get(producer: ResourceProducer): List<FoodChainEdge> {
        return elements.find { it.producer == producer }?.consumer ?: emptyList()
    }

    operator fun get(consumer: ResourceConsumer): List<ResourceProducer> {
        return emptyList()

    }

    fun add(poc: PorC): FoodChain {
        add(poc as ResourceProducer)
        add(poc as ResourceConsumer)
        return this
    }

    fun add(producer: ResourceProducer): FoodChain {
        elements += FoodChainNode(this, producer)
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

    private fun getPossibleFoodSources(consumer: ResourceConsumer) = elements.filter { consumer.canConsume(it.producer) }

    /**
     * We need to check consumers without prdocuers if the new producer matchers their needs and add them to the producers consume list
     */
    private fun addConsumersWithoutFoodsource(producer: ResourceProducer) {
        consumersWithoutProducers.filter { it.canConsume(producer) }.forEach { add(it) }
        consumersWithoutProducers.removeIf { it.canConsume(producer) }
    }

    /**
     * Get the graph in dot notation.
     * @link https://www.graphviz.org/doc/info/lang.html
     */
    fun asDotNotation(): String {
        val sb = StringBuilder("digraph G {\n")
        elements
            .map { asDotNotation(it).joinToString("\n") }
            .filter { it.isNotBlank() && it.isNotEmpty() }
            .joinToString("\n")
            .let { sb.append(it) }
        sb.append("\n}")
        return sb.toString()
    }

    /**
     * Represent a node in dot notation
     */
    private fun asDotNotation(node: FoodChainNode): Iterable<String> = node.consumer.map { "${node.producer.name} -> ${it.consumer.name}" }

}

/**
 * Nodes in the food chain are producers and connections to their consumers
 */
private data class FoodChainNode(private val foodChain: FoodChain, val producer: ResourceProducer) {
    val consumer: MutableList<FoodChainEdge> = mutableListOf()
    val fittnessCalculator = FittnessCalculator(producer)

    /**
     * add a new connection between the nodes producer and the given producer.
     */
    fun add(newConsumer: ResourceConsumer): FoodChainNode {
        consumer += FoodChainEdge(
            newConsumer,
            fittnessCalculator.calculate(foodChain[producer].map { it.consumer })
            // add other weight / property calculators here
        )
        return this
    }
}

/**
 * Edge in the food chain connect producers to their consumers. Each edge has properties describing the order and (fill in later when implemented) other qualities of the connection
 */
data class FoodChainEdge(val consumer: ResourceConsumer, val fittness: Double) {


}



