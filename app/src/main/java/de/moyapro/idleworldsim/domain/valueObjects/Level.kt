package de.moyapro.idleworldsim.domain.valueObjects

class Level(val level: Int) : Comparable<Level> {
    override fun compareTo(other: Level) = level.compareTo(other.level)
}
