package de.moyapro.idleworldsim.domain.consumption

import de.blox.graphview.Graph
import de.blox.graphview.tree.BuchheimWalkerAlgorithm
import de.blox.graphview.tree.BuchheimWalkerConfiguration
import de.moyapro.idleworldsim.domain.Species
import de.moyapro.idleworldsim.domain.traits.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

@Suppress("USELESS_CAST")
internal class FoodChainTest {

    private val producer1: ResourceProducer = Species("p1", Feature(Meaty()))
    private val producer2: ResourceProducer = Species("p2", Feature.sunlightConsumer())
    private val consumer1: ResourceConsumer = Species("c1", Feature(Predator(Meaty())))
    private val consumer2: ResourceConsumer = Species("c2", Feature.oxygenConsumer())
    private val poc1 = Species("pc1", Feature(Predator(Meaty())), Feature.sunlightConsumer())
    private val poc2 = Species("pc2", Feature(Predator(Meaty())), Feature.sunlightConsumer())

    @Test
    fun insertIntoFoodChain() {
        val foodChain = buildTestFoodchain()
        val species = Species("Any")
        foodChain.add(species)
        assertThat(foodChain[poc1 as ResourceProducer].size).isEqualTo(1)
        assertThat(foodChain[poc2 as ResourceProducer].size).isEqualTo(1)
        assertThat(foodChain.producers().size).isEqualTo(5)
        assertThat(foodChain[producer1 as ResourceProducer].size).isEqualTo(3)
        assertThat(foodChain[producer2 as ResourceProducer].size).isEqualTo(1)
    }

    @Test
    fun addProducerAfterConsumer() {
        val foodChain = FoodChain()
            .add(poc2)
            .add(consumer2)
            .add(producer2)
        assertThat(foodChain[producer2 as ResourceProducer].map { it.consumer }).contains(consumer2)
        assertThat(foodChain[consumer2 as ResourceConsumer].map { it.producer }).contains(producer2)
    }

    @Test
    fun addConsumerAfterProducer() {
        val foodChain = FoodChain()
            .add(poc1)
            .add(producer1)
            .add(consumer1)
        assertThat(foodChain[producer1 as ResourceProducer].map { it.consumer }).contains(consumer1)
        assertThat(foodChain[consumer1 as ResourceConsumer].map { it.producer }).contains(producer1)
    }

    @Test
    fun prevendDoubleAddConsumer() {
        val foodChain = FoodChain()
            .add(producer1)
            .add(consumer1)
            .add(consumer1)
        assertThat(foodChain[producer1 as ResourceProducer].size).isEqualTo(1)
        assertThat(foodChain[consumer1 as ResourceConsumer].size).isEqualTo(1)
    }

    @Test
    fun prevendDoubleAddProducer() {
        val foodChain = FoodChain()
            .add(producer1)
            .add(consumer1)
            .add(producer1)
        assertThat(foodChain[producer1 as ResourceProducer].size).isEqualTo(1)
        assertThat(foodChain[consumer1 as ResourceConsumer].size).isEqualTo(1)
    }


    @Test
    fun generatedotNotation() {
        val dotNotation = buildTestFoodchain().asDotNotation()
        assertThat(dotNotation).contains("digraph G {")
        assertThat(dotNotation).contains("p1 -> c1")
        assertThat(dotNotation).contains("pc1 -> c2")
        assertThat(dotNotation).contains("pc2 -> c2")
        assertThat(dotNotation).contains("}")
    }

    @Test
    fun createGraphFromFoodchain() {
        val graph: Graph = FoodChain().add(producer1).add(consumer1).generateGraph()
        assertThat(graph.nodeCount).isEqualTo(2)
        assertThat(graph.edges.size).isEqualTo(1)
        assertThat(graph.nodes.map { it.data }).contains(producer1)
        assertThat(graph.nodes.map { it.data }).contains(consumer1)
        assertThat(graph.edges[0].source.data).isEqualTo(producer1)
        assertThat(graph.edges[0].destination.data).isEqualTo(consumer1)
    }

    @Test
    fun graphWorking() {
        val graph: Graph = FoodChain().add(producer1).add(consumer1).generateGraph()
        val configuration: BuchheimWalkerConfiguration = BuchheimWalkerConfiguration.Builder()
            .setSiblingSeparation(100)
            .setLevelSeparation(300)
            .setSubtreeSeparation(300)
            .setOrientation(BuchheimWalkerConfiguration.ORIENTATION_TOP_BOTTOM)
            .build()
        val layoutAlgorithm = BuchheimWalkerAlgorithm(configuration)

        layoutAlgorithm.run(graph, 0f, 0f)
    }


    private fun buildTestFoodchain(): FoodChain {
        return FoodChain()
            .add(producer1)
            .add(producer2)
            .add(consumer1)
            .add(consumer2)
            .add(poc1)
            .add(poc2)
    }
}
