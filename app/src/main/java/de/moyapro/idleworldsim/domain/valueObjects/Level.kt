package de.moyapro.idleworldsim.domain.valueObjects

import de.moyapro.idleworldsim.domain.traits.Trait

class Level(val level: Int) : Comparable<Level> {
    override fun compareTo(other: Level) = level.compareTo(other.level)

    override fun equals(other: Any?): Boolean {
        if (null == other || other !is Level) {
            return false
        }
        return this.level == other.level
    }

    override fun hashCode() = this.level.hashCode() * 67
    operator fun minus(other: Level) = Level(this.level - other.level)
    operator fun plus(other: Level) = Level(this.level + other.level)

    override fun toString(): String {
        return "Level[$level]"
    }
}


fun sum(traits: Iterable<Trait>): Level {
    return Level(traits.map { it.level.level }.sum())
}