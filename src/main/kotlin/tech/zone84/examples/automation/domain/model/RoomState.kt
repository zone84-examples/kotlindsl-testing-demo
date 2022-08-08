package tech.zone84.examples.automation.domain.model

data class RoomState(
    val room: Room,
    val readings: Map<Sensor, Measure>
)
