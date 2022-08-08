package tech.zone84.examples.automation.domain.engine

import mu.KotlinLogging
import tech.zone84.examples.automation.domain.model.Device
import tech.zone84.examples.automation.domain.model.RoomState
import tech.zone84.examples.automation.domain.model.Selector
import tech.zone84.examples.automation.domain.model.Sensor

interface MonitoringRule {
    fun trigger(state: RoomState): List<ActionCommand>
}

class MaxTemperature(
    val threshold: Double,
    val sensors: Selector<Sensor>,
    val devices: Selector<Device>
) : MonitoringRule {
    override fun trigger(state: RoomState): List<ActionCommand> {
        val sensorList = sensors.select(state)
        val average = sensorList
            .filter { state.readings[it] != null }
            .map { state.readings[it]!!.value }
            .average()

        logger.debug { "${state.room.name}: computed average temperature '$average' from sensor(s) [${sensorList.joinToString { it.name } }]" }

        return if (average <= threshold) {
            devices.select(state)
                .filter { it.enabled }
                .map { DeviceTurnOffCommand(state.room, it) }
                .toList()
        } else {
            devices.select(state)
                .filterNot { it.enabled }
                .map { DeviceTurnOnCommand(state.room, it) }
                .toList()
        }
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}

class MinTemperature(
    val threshold: Double,
    val sensors: Selector<Sensor>,
    val devices: Selector<Device>
) : MonitoringRule {
    override fun trigger(state: RoomState): List<ActionCommand> {
        val sensorList = sensors.select(state)
        val average = sensorList
            .filter { state.readings[it] != null }
            .map { state.readings[it]!!.value }
            .average()

        logger.debug { "${state.room.name}: computed average temperature '$average' from sensor(s) [${sensorList.joinToString { it.name } }]" }

        return if (average >= threshold) {
            devices.select(state)
                .filter { it.enabled }
                .map { DeviceTurnOffCommand(state.room, it) }
                .toList()
        } else {
            devices.select(state)
                .filterNot { it.enabled }
                .map { DeviceTurnOnCommand(state.room, it) }
                .toList()
        }
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
