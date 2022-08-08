package tech.zone84.examples.automation.domain.port

import tech.zone84.examples.automation.domain.model.Measure

interface SensorClient {
    val address: Int
    fun nextMeasure(): Measure?
}
