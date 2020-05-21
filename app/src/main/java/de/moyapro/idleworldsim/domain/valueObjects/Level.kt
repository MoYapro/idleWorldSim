package de.moyapro.idleworldsim.domain.valueObjects

class Level(val level: Int) : Comparable<Level> {
    override fun compareTo(other: Level) = level.compareTo(other.level)

    override fun equals(other: Any?): Boolean {
        if (null == other || other !is Level) {
            return false
        }
        return this.level == other.level
    }

    override fun hashCode() = this.level.hashCode() * 67
}
