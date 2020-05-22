package de.moyapro.idleworldsim.domain.consumption

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

internal class FoodChainTest {

    private val producer1: ResourceProducer = DummyProducer("p1")
    private val producer2: ResourceProducer = DummyProducer("p2")
    private val consumer1: ResourceConsumer = DummyConsumer("c1").canConsume("p1")
    private val consumer2: ResourceConsumer = DummyConsumer("c1").canConsume("pc1")
    private val poc: PorC = DummyPorC("pc1").canConsume("p1")

    @Test
    fun createFoodChain() {
    val foodChain = FoodChain()
        .add(producer1)
        .add(producer2)
        .add(consumer1)
        .add(consumer2)
        .add(poc)
        assertThat(foodChain[poc].size).isEqualTo(1)
        assertThat(foodChain.producers()).isEqualTo(3)
        assertThat(foodChain[producer1].size).isEqualTo(2)
        assertThat(foodChain[producer2].size).isEqualTo(0)
    }
}
