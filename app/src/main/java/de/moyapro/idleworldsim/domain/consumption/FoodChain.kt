package de.moyapro.idleworldsim.domain.consumption

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
        return elements.find { it.producer == producer }?.consumers ?: emptyList()
    }

    operator fun get(consumer: ResourceConsumer): List<ResourceProducer> {
        return elements
            .filter { node -> nodeConatinsConsumer(node, consumer) }
            .map { it.producer }
    }

    private fun nodeConatinsConsumer(
        it: FoodChainNode,
        consumer: ResourceConsumer
    ) = it.consumers.any { it.consumer == consumer }

    fun add(poc: PorC): FoodChain {
        add(poc as ResourceProducer)
        add(poc as ResourceConsumer)
        return this
    }

    fun add(producer: ResourceProducer): FoodChain {
        if (isProducerAlreadyInFoodchain(producer)) {
            return this // do not add producer again
        }
        val newProducerNode = FoodChainNode(this, producer)
        elements += newProducerNode
        findConsumersFor(producer).forEach { newProducerNode.add(it) }
        addConsumersWithoutFoodsource(producer)
        return this
    }

    private fun isProducerAlreadyInFoodchain(producer: ResourceProducer) =
        elements.any { it.producer == producer }

    private fun findConsumersFor(producer: ResourceProducer): Iterable<ResourceConsumer> {
        return elements
            .map { node ->
                node.consumers.filter {
                    it.consumer.canConsume(producer)
                }
                    .map { it.consumer }
            }
            .flatten()
    }

    fun producers(): Int {
        return elements.size
    }

    fun add(consumer: ResourceConsumer): FoodChain {
        if (isConsumerAlreadyInFoodchain(consumer)) {
            return this // do not add producer again
        }
        val foodSources = getPossibleFoodSources(consumer)
        if (foodSources.isNotEmpty()) {
            addConsumer(foodSources, consumer)
        } else {
            consumersWithoutProducers += consumer
        }
        return this
    }

    private fun isConsumerAlreadyInFoodchain(consumer: ResourceConsumer): Boolean {
        return elements.any { node -> node.consumers.any { edge -> edge.consumer == consumer } }
    }

    private fun addConsumer(foodSources: List<FoodChainNode>, consumer: ResourceConsumer) {
        foodSources.forEach { it.add(consumer) }
    }

    private fun getPossibleFoodSources(consumer: ResourceConsumer) =
        elements.filter { consumer.canConsume(it.producer) }

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
    private fun asDotNotation(node: FoodChainNode): Iterable<String> =
        node.consumers.map { "${node.producer.name} -> ${it.consumer.name}" }

}

/**
 * Nodes in the food chain are producers and connections to their consumers
 */
private data class FoodChainNode(private val foodChain: FoodChain, val producer: ResourceProducer) {
    val consumers: MutableList<FoodChainEdge> = mutableListOf()
    val fittnessCalculator = FittnessCalculator(producer)

    /**
     * add a new connection between the nodes producer and the given producer.
     */
    fun add(newConsumer: ResourceConsumer): FoodChainNode {
        if (isConsumerAlreadyAdded(newConsumer)) {
            return this // do not re-add
        }
        consumers += FoodChainEdge(
            newConsumer,
            0.0
        )

        updateConsumerWeights(this)

        return this
    }

    private fun updateConsumerWeights(foodChainNode: FoodChainNode) {
        val absoluteConsumeSkill = foodChainNode.consumers
            .associateBy(
                { it.consumer },
                { it.consumer.consumePowerIndex(foodChainNode.producer) }
            )
        val maxConsumeSkill = absoluteConsumeSkill.values.max() ?: 0.0

        val relativeConsumeValuePerSpecies = absoluteConsumeSkill
            .map { (consumer, consumeSkill) -> Pair(consumer, maxConsumeSkill - consumeSkill) }
            .associate { it }

        relativeConsumeValuePerSpecies.entries
            .sortedBy { it.value }
            .map() { (consumer, _) -> consumer }
            .withIndex()
            .forEach() { (rank, consumer) ->
                foodChainNode[consumer]?.consumeFactor = calculateConsumeFactor(rank)
            }

    }

    private operator fun get(consumer: ResourceConsumer): FoodChainEdge? =
        this.consumers.first { it.consumer == consumer } // consumer is unique


    private fun calculateConsumeFactor(rank: Int) = 1.0 / ((rank) * 2)


    private fun isConsumerAlreadyAdded(newConsumer: ResourceConsumer): Boolean {
        return consumers.any { it.consumer == newConsumer }
    }
}

/**
 * Edge in the food chain connect producers to their consumers. Each edge has properties describing the order and (fill in later when implemented) other qualities of the connection
 */
data class FoodChainEdge(val consumer: ResourceConsumer, val fittness: Double) {
    var consumeFactor: Double = 0.0
}



