package tech.zone84.examples.automation.dsl

import com.google.common.collect.ImmutableList
import tech.zone84.examples.automation.domain.engine.MonitoringRule
import tech.zone84.examples.automation.domain.model.Measure
import tech.zone84.examples.automation.domain.model.MeasureUnit
import tech.zone84.examples.automation.domain.model.MeasureUnit.HUMIDITY_PERCENTAGE
import tech.zone84.examples.automation.domain.model.MeasureUnit.TEMP_CELSIUS
import tech.zone84.examples.automation.dsl.DeviceType.*
import tech.zone84.examples.automation.dsl.SensorType.HUMIDITY
import tech.zone84.examples.automation.dsl.SensorType.TEMPERATURE

class AutomationDefinition : AutomationDsl {
    private val roomDefinitions: MutableList<RoomDefinition> = ArrayList()
    private var rules: List<MonitoringRule> = emptyList()

    override fun room(name: String, dsl: RoomDsl.() -> Unit) {
        roomDefinitions += RoomDefinition(name).apply(dsl)
    }

    override fun rules(rules: List<MonitoringRule>) {
        this.rules = ImmutableList.copyOf(rules)
    }

    fun installWith(processor: AutomationStrategy) {
        processor.process(roomDefinitions, rules)
    }
}

class RoomDefinition(val name: String) : RoomDsl {
    private val monitoringStationDefinitions: MutableList<MonitoringStationDefinition> = ArrayList()
    private val deviceDefinitions: MutableList<DeviceDefinition> = ArrayList()

    override fun monitoringStation(name: String, address: String, dsl: MonitoringStationDsl.() -> Unit) {
        monitoringStationDefinitions += MonitoringStationDefinition(name, address).apply(dsl)
    }

    override fun fan(name: String) {
        deviceDefinitions += DeviceDefinition(name, FAN)
    }

    override fun dehumidifier(name: String) {
        deviceDefinitions += DeviceDefinition(name, DEHUMIDIFIER)
    }

    override fun heater(name: String) {
        deviceDefinitions += DeviceDefinition(name, HEATER)
    }

    fun fetchMonitoringStations() = ImmutableList.copyOf(monitoringStationDefinitions)

    fun fetchDevices() = ImmutableList.copyOf(deviceDefinitions)
}

class MonitoringStationDefinition(val name: String, val address: String) : MonitoringStationDsl {
    private val sensors: MutableList<SensorDefinition> = ArrayList()

    override fun temperatureSensor(name: String, address: Int, dsl: SensorDsl.() -> Unit) {
        sensors += SensorDefinition(name, address, TEMPERATURE).apply(dsl)
    }

    override fun humiditySensor(name: String, address: Int, dsl: SensorDsl.() -> Unit) {
        sensors += SensorDefinition(name, address, HUMIDITY).apply(dsl)
    }

    fun fetchSensors() = ImmutableList.copyOf(sensors)
}

class SensorDefinition(val name: String, val address: Int, val type: SensorType) : SensorDsl {
    private var readings: List<Measure> = emptyList()

    override fun readings(values: Sequence<Double>) {
        this.readings = values
            .map { Measure(it, type.unit) }
            .toList()
    }

    fun fetchReadings(): ImmutableList<Measure> = ImmutableList.copyOf(readings)
}

data class DeviceDefinition(val name: String, val type: DeviceType)

enum class DeviceType {
    FAN, DEHUMIDIFIER, HEATER
}

enum class SensorType(val unit: MeasureUnit) {
    TEMPERATURE(TEMP_CELSIUS), HUMIDITY(HUMIDITY_PERCENTAGE)
}
