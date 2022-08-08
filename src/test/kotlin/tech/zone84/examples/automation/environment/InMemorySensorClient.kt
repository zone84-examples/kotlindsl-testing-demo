package tech.zone84.examples.automation.environment

import com.google.common.collect.ImmutableList
import tech.zone84.examples.automation.domain.model.Measure
import tech.zone84.examples.automation.domain.port.SensorClient

class InMemorySensorClient(
    override val address: Int,
    val measures: ImmutableList<Measure>
) : SensorClient {
    val iterator = measures.iterator()

    override fun nextMeasure(): Measure? {
        if (iterator.hasNext()) {
            return iterator.next()
        }
        return null
    }
}