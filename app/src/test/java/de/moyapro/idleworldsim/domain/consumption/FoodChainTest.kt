package de.moyapro.idleworldsim.domain.consumption

import de.moyapro.idleworldsim.domain.two.Species
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import kotlin.test.fail

internal class FoodChainTest {

    private val producer1: ResourceProducer = DummyProducer("p1")
    private val producer2: ResourceProducer = DummyProducer("p2")
    private val consumer1: ResourceConsumer = DummyConsumer("c1").canConsume("p1")
    private val consumer2: ResourceConsumer =
        DummyConsumer("c2").canConsume("pc2").canConsume("pc1").canConsume("p2")
    private val poc1: PorC = DummyPorC("pc1").canConsume("p1")
    private val poc2: PorC = DummyPorC("pc2").canConsume("p1")

    @Test
    fun insertIntoFoodChain() {
        val foodChain = buildTestFoodchain()
        val species: Species = DummyPorC("Any")
        foodChain.add(species)
        assertThat(foodChain[poc1 as ResourceProducer].size).isEqualTo(1)
        assertThat(foodChain[poc2 as ResourceProducer].size).isEqualTo(1)
        assertThat(foodChain.producers()).isEqualTo(5)
        assertThat(foodChain[producer1].size).isEqualTo(3)
        assertThat(foodChain[producer2].size).isEqualTo(1)
    }

    @Test
    fun addProducerAfterConsumer() {
        val foodChain = FoodChain()
            .add(poc2)
            .add(consumer2)
            .add(producer2)
        assertThat(foodChain[producer2].map { it.consumer }).contains(consumer2)
        assertThat(foodChain[consumer2].map { it.producer }).contains(producer2)
    }

    @Test
    fun addConsumerAfterProducer() {
        val foodChain = FoodChain()
            .add(poc1)
            .add(producer1)
            .add(consumer1)
        assertThat(foodChain[producer1].map { it.consumer }).contains(consumer1)
        assertThat(foodChain[consumer1].map { it.producer }).contains(producer1)
    }

    @Test
    fun prevendDoubleAddConsumer() {
        val foodChain = FoodChain()
            .add(producer1)
            .add(consumer1)
            .add(consumer1)
        assertThat(foodChain[producer1].size).isEqualTo(1)
        assertThat(foodChain[consumer1].size).isEqualTo(1)
    }

    @Test
    fun prevendDoubleAddProducer() {
        val foodChain = FoodChain()
            .add(producer1)
            .add(consumer1)
            .add(producer1)
        assertThat(foodChain[producer1].size).isEqualTo(1)
        assertThat(foodChain[consumer1].size).isEqualTo(1)
    }


    @Test
    fun weights() {
        val foodChain = buildTestFoodchain()
        val consumers = foodChain[producer1]
//        assertThat(consumers[0].fittness).`as`("First consumer should have higher consume fittness")
//            .isGreaterThanOrEqualTo(consumers[1].fittness)
        fail("Implement me")
    }

    @Test
    fun dotNotation() {
        val dotNotation = buildTestFoodchain().asDotNotation()
        assertThat(dotNotation).contains("digraph G {")
        assertThat(dotNotation).contains("p1 -> c1")
        assertThat(dotNotation).contains("pc1 -> c2")
        assertThat(dotNotation).contains("pc2 -> c2")
        assertThat(dotNotation).contains("}")
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
