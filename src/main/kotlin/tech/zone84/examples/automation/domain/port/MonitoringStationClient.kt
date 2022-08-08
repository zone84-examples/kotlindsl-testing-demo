package tech.zone84.examples.automation.domain.port

interface MonitoringStationClient {
    fun fetchSensorClients(stationAddress: String, sensors: Collection<Int>) : Map<Int, SensorClient>
}
