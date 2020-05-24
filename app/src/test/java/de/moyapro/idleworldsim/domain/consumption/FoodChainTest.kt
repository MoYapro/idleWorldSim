package de.moyapro.idleworldsim.domain.consumption

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class FoodChainTest {

    private val producer1: ResourceProducer = DummyProducer("p1")
    private val producer2: ResourceProducer = DummyProducer("p2")
    private val consumer1: ResourceConsumer = DummyConsumer("c1").canConsume("p1")
    private val consumer2: ResourceConsumer = DummyConsumer("c2").canConsume("pc1").canConsume("pc2")
    private val poc1: PorC = DummyPorC("pc1").canConsume("p1")
    private val poc2: PorC = DummyPorC("pc2").canConsume("p1")

    @Test
    fun insertIntoFoodChain() {
        val foodChain = buildTestFoodchain()
        assertThat(foodChain[poc1 as ResourceProducer].size).isEqualTo(1)
        assertThat(foodChain.producers()).isEqualTo(4)
        assertThat(foodChain[producer1].size).isEqualTo(3)
        assertThat(foodChain[producer2].size).isEqualTo(0)
    }

    @Test
    fun weights() {
        val foodChain = buildTestFoodchain()
        val consumers = foodChain[producer1]
        assertThat(consumers[0].fittness).`as`("First consumer should have higher consume fittness")
            .isGreaterThanOrEqualTo(consumers[1].fittness)
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
