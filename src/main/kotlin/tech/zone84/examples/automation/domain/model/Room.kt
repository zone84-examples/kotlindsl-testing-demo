package tech.zone84.examples.automation.domain.model

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import tech.zone84.examples.automation.domain.port.MonitoringStationClient

data class Room(
    val name: String,
    val stations: ImmutableList<MonitoringStation>,
    val devices: ImmutableList<Device>,
) {
    fun fetchDevice(name: String): Device = devices
        .firstOrNull { it.name == name }
        ?: throw IllegalArgumentException("No such device '$name' in room '${this.name}'")

    fun computeRoomState(client: MonitoringStationClient): RoomState {
        val readings = ImmutableMap.builder<Sensor, Measure>()
        stations
            .asSequence()
            .flatMap { it.collectReadings(client).asSequence() }
            .forEach { readings.put(it.key, it.value) }

        return RoomState(this, readings.build())
    }
}
