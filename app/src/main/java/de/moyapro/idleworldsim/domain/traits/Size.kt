package de.moyapro.idleworldsim.domain.traits

import de.moyapro.idleworldsim.domain.consumption.Resources
import de.moyapro.idleworldsim.domain.valueObjects.Level

class Size(size: Int = 1) : Trait(Level(size)) {
    override fun getConsumptionResources(size: Size?): Resources =
        Resources(this.level.level)

}
