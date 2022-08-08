package tech.zone84.examples.automation.domain.model

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import tech.zone84.examples.automation.domain.port.MonitoringStationClient

data class MonitoringStation(
    val name: String,
    val address: String,
    val sensors: ImmutableList<Sensor>
) {
    private val byAddress: ImmutableMap<Int, Sensor> = sensors
        .associateBy({ it.address }) { it }
        .let { ImmutableMap.copyOf(it) }

    fun collectReadings(client: MonitoringStationClient): Map<Sensor, Measure> {
        val result = LinkedHashMap<Sensor, Measure>()
        for ((address, sensorClient) in client.fetchSensorClients(address, byAddress.keys)) {
            val clientMeasure = sensorClient.nextMeasure()
            if (clientMeasure != null) {
                result.put(byAddress[address]!!, clientMeasure)
            }
        }
        return result
    }
}
