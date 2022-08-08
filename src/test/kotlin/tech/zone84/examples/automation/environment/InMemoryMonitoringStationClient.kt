package tech.zone84.examples.automation.environment

import com.google.common.collect.LinkedListMultimap
import com.google.common.collect.Multimap
import tech.zone84.examples.automation.domain.port.MonitoringStationClient
import tech.zone84.examples.automation.domain.port.SensorClient

class InMemoryMonitoringStationClient : MonitoringStationClient {
    private var sensorClients: Multimap<String, InMemorySensorClient> = LinkedListMultimap.create()

    fun installClients(clients: Multimap<String, InMemorySensorClient>) {
        this.sensorClients = clients
    }

    override fun fetchSensorClients(stationAddress: String, sensors: Collection<Int>): Map<Int, SensorClient> {
        return sensorClients[stationAddress]
            .asSequence()
            .filter { it.address in sensors }
            .associateBy { it.address }
    }
}
