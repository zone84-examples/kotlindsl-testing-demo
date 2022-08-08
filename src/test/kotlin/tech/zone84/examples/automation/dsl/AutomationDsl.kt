package tech.zone84.examples.automation.dsl

import tech.zone84.examples.automation.domain.engine.MonitoringRule

@AutomationDslMarker
interface AutomationDsl {
    @AutomationDslMarker
    fun room(name: String, dsl: RoomDsl.() -> Unit)

    @AutomationDslMarker
    fun rules(rules: List<MonitoringRule>)

    @AutomationDslMarker
    fun rules(vararg rules: MonitoringRule) = rules(rules.toList())
}

@AutomationDslMarker
interface RoomDsl {
    @AutomationDslMarker
    fun monitoringStation(name: String, address: String, dsl: MonitoringStationDsl.() -> Unit)

    @AutomationDslMarker
    fun fan(name: String)

    @AutomationDslMarker
    fun dehumidifier(name: String)

    @AutomationDslMarker
    fun heater(name: String)
}

@AutomationDslMarker
interface MonitoringStationDsl {
    @AutomationDslMarker
    fun temperatureSensor(name: String, address: Int, dsl: SensorDsl.() -> Unit)

    @AutomationDslMarker
    fun humiditySensor(name: String, address: Int, dsl: SensorDsl.() -> Unit)
}

@AutomationDslMarker
interface SensorDsl {
    @AutomationDslMarker
    fun readings(vararg values: Int) = readings(values.asSequence().map { it.toDouble() })

    @AutomationDslMarker
    fun readings(vararg values: Double) = readings(values.asSequence())

    @AutomationDslMarker
    fun readings(values: List<Double>) = readings(values.asSequence())

    @AutomationDslMarker
    fun readings(values: Sequence<Double>)
}

@DslMarker
annotation class AutomationDslMarker