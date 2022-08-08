package tech.zone84.examples.automation.dsl

import com.google.common.collect.ImmutableList
import com.google.common.collect.LinkedListMultimap
import com.google.common.collect.Multimap
import tech.zone84.examples.automation.domain.engine.MonitoringRule
import tech.zone84.examples.automation.domain.model.*
import tech.zone84.examples.automation.environment.InMemoryMonitoringStationClient
import tech.zone84.examples.automation.environment.InMemorySensorClient
import tech.zone84.examples.automation.environment.ModelDictionary

class UnitTestAutomationStrategy(private val target: ModelDictionary, private val client: InMemoryMonitoringStationClient) : AutomationStrategy {
    override fun process(rooms: List<RoomDefinition>, rules: List<MonitoringRule>) {
        val roomModels = rooms.map {
            Room(
                name = it.name,
                stations = processMonitoringStations(it.fetchMonitoringStations()),
                devices = processDevices(it.fetchDevices())
            )
        }
        val clients = createClients(rooms)

        client.installClients(clients)
        target.installModel(roomModels, rules)
    }

    private fun processMonitoringStations(monitoringStations: List<MonitoringStationDefinition>): ImmutableList<MonitoringStation> = monitoringStations
        .map {
            MonitoringStation(
                name = it.name,
                address = it.address,
                sensors = processSensors(it.fetchSensors())
            )
        }
        .let { ImmutableList.copyOf(it) }

    private fun processSensors(sensors: List<SensorDefinition>): ImmutableList<Sensor> = sensors
        .map {
            when (it.type) {
                SensorType.TEMPERATURE -> TemperatureSensor(it.name, it.address)
                SensorType.HUMIDITY -> HumiditySensor(it.name, it.address)
            }
        }
        .let { ImmutableList.copyOf(it) }

    private fun processDevices(devices: List<DeviceDefinition>): ImmutableList<Device> = devices
        .map {
            when (it.type) {
                DeviceType.FAN -> FanDevice(it.name)
                DeviceType.HEATER -> HeaterDevice(it.name)
                DeviceType.DEHUMIDIFIER -> DehumidifierDevice(it.name)
            }
        }
        .let { ImmutableList.copyOf(it) }

    private fun createClients(rooms: List<RoomDefinition>): Multimap<String, InMemorySensorClient> {
        val builder = LinkedListMultimap.create<String, InMemorySensorClient>()
        for (room in rooms) {
            for (station in room.fetchMonitoringStations()) {
                for (sensor in station.fetchSensors()) {
                    builder.put(station.address, InMemorySensorClient(sensor.address, sensor.fetchReadings()))
                }
            }
        }
        return builder
    }
}
