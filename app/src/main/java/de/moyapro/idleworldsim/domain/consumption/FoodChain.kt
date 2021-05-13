package de.moyapro.idleworldsim.domain.consumption

import de.blox.graphview.Edge
import de.blox.graphview.Graph
import de.blox.graphview.Node
import de.moyapro.idleworldsim.domain.Species

/**
 * Implement the food chain of a given set of producers and consumers.
 */
class FoodChain {

    private val nodes: MutableList<FoodChainNode> = mutableListOf()
    private val consumersWithoutProducers: MutableList<ResourceConsumer> = mutableListOf()

    /**
     * Get all FoodChainEdge to consumers of the given producer
     */
    operator fun get(producer: ResourceProducer): List<FoodChainEdge> {
        return nodes.find { it.producer == producer }?.consumers ?: emptyList()
    }

    operator fun get(consumer: ResourceConsumer): List<FoodChainEdge> {
        return nodes
            .filter { node -> nodeConatinsConsumer(node, consumer) }
            .map { it.consumers }
            .flatten()
    }

    private fun nodeConatinsConsumer(
        it: FoodChainNode,
        consumer: ResourceConsumer
    ) = it.consumers.any { it.consumer == consumer }

    fun add(poc: Species): FoodChain {
        add(poc as ResourceProducer)
        add(poc as ResourceConsumer)
        return this
    }

    fun add(producer: ResourceProducer): FoodChain {
        if (isProducerAlreadyInFoodchain(producer)) {
            return this // do not add producer again
        }
        val newProducerNode = FoodChainNode(this, producer)
        nodes += newProducerNode
        findConsumersOf(producer).forEach { newProducerNode.add(producer, it) }
        addConsumersWithoutFoodsource(producer)
        updateConsumersFoodPreferences()
        return this
    }

    private fun updateConsumersFoodPreferences() {
        val consumerMap: Map<ResourceConsumer, List<FoodChainEdge>> = getConsumerMap()
        updateConsumersFoodPreferences(consumerMap)
    }

    private fun updateConsumersFoodPreferences(consumerMap: Map<ResourceConsumer, List<FoodChainEdge>>) {
        consumerMap.forEach { (consumer, producers) ->
            updateConsumersFoodPreferences(
                consumer,
                producers
            )
        }
    }

    private fun updateConsumersFoodPreferences(
        consumer: ResourceConsumer,
        producers: List<FoodChainEdge>
    ) {
        producers.forEach { producerEdge ->
            producerEdge.consumerPreference = consumer.calculatePreferenceIndex(producerEdge.producer, producerEdge.consumeFactor)
        }
    }

    private fun getConsumerMap(): Map<ResourceConsumer, List<FoodChainEdge>> {
        return nodes
            .map { it.consumers }
            .flatten()
            .groupBy(
                { it.consumer },
                { it }
            )
    }

    private fun isProducerAlreadyInFoodchain(producer: ResourceProducer) =
        nodes.any { it.producer == producer }

    private fun findConsumersOf(producer: ResourceProducer): Iterable<ResourceConsumer> {
        return nodes
            .map { node ->
                node.consumers.filter {
                    it.consumer.canConsume(producer)
                }
                    .map { it.consumer }
            }
            .flatten()
    }

    fun producers(): List<ResourceProducer> {
        return nodes
            .map { it.producer }
            .distinct()
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
        return nodes.any { node -> node.consumers.any { edge -> edge.consumer == consumer } }
    }

    private fun addConsumer(foodSources: List<FoodChainNode>, consumer: ResourceConsumer) {
        foodSources.forEach { it.add(it.producer, consumer) }
    }

    private fun getPossibleFoodSources(consumer: ResourceConsumer) =
        nodes.filter { consumer.canConsume(it.producer) }

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
        nodes
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

    fun getRelations(): List<FoodChainEdge> {
        val unrelated = consumersWithoutProducers.map { consumerWithoutProducer -> FoodChainEdge(NoghingProducer, consumerWithoutProducer, 0.0, 0.0) }
        val related = nodes
            .map { it.consumers }
            .flatten()

        return unrelated + related
    }

    fun generateGraph(): Graph {
        val graph = Graph()
        nodes.forEach { producerConsumerRelation ->
            val producerNode = Node(producerConsumerRelation.producer)
            graph.addNode(producerNode)
            producerConsumerRelation.consumers.forEach { consumer ->
                val consumerNode = Node(consumer.consumer)
                graph.addNode(consumerNode)
                graph.addEdge(Edge(producerNode, consumerNode))
            }
        }
        consumersWithoutProducers.forEach {
            graph.addNode(Node(it))
        }
        return graph
    }

}

object NoghingProducer : Species("Nothing to Eat") {
    override fun getResourcesPerInstance() = emptyResources()
}

/**
 * Nodes in the food chain are producers and connections to their consumers
 */
private data class FoodChainNode(private val foodChain: FoodChain, val producer: ResourceProducer) {
    val consumers: MutableList<FoodChainEdge> = mutableListOf()

    /**
     * add a new connection between the nodes producer and the given producer.
     */
    fun add(producer: ResourceProducer, newConsumer: ResourceConsumer): FoodChainNode {
        if (isConsumerAlreadyAdded(newConsumer)) {
            return this // do not re-add
        }
        consumers += FoodChainEdge(producer, newConsumer)
        updateConsumerWeights(this)
        return this
    }

    private fun updateConsumerWeights(foodChainNode: FoodChainNode) {
        val absoluteConsumeSkill = foodChainNode.consumers
            .associateBy(
                { it.consumer },
                { it.consumer.consumePowerFactor(foodChainNode.producer) }
            )
        val maxConsumeSkill = absoluteConsumeSkill.values.max() ?: 0.0

        val relativeConsumeValuePerSpecies = absoluteConsumeSkill
            .map { (consumer, consumeSkill) -> Pair(consumer, maxConsumeSkill - consumeSkill) }
            .associate { it }

        relativeConsumeValuePerSpecies.entries
            .sortedBy { it.value }
            .map { (consumer, _) -> consumer }
            .withIndex()
            .forEach { (rank, consumer) ->
                foodChainNode[consumer]?.consumeFactor = calculateConsumeFactor(rank + 1)
            }

    }

    private operator fun get(consumer: ResourceConsumer): FoodChainEdge =
        this.consumers.first { it.consumer == consumer } // consumer is unique


    private fun calculateConsumeFactor(rank: Int) = 1.0 / ((rank) * 2)


    private fun isConsumerAlreadyAdded(newConsumer: ResourceConsumer): Boolean {
        return consumers.any { it.consumer == newConsumer }
    }

    override fun toString(): String {
        return "FoodChainNode[$producer -> ${consumers.joinToString(",")}"
    }

}

/**
 * Edge in the food chain connect producers to their consumers. Each edge has properties describing the order and (fill in later when implemented) other qualities of the connection
 */
data class FoodChainEdge(
    val producer: ResourceProducer,
    val consumer: ResourceConsumer,
    var consumerPreference: Double = 0.0,
    var consumeFactor: Double = 0.0
)



