package tech.zone84.examples.automation.domain.engine

import tech.zone84.examples.automation.domain.model.Room
import tech.zone84.examples.automation.domain.port.MonitoringStationClient

class StepExecutor(
    private val rooms: List<Room>,
    private val rules: List<MonitoringRule>
) {
    fun executeStep(monitoringStationClient: MonitoringStationClient): List<ActionCommand> {
        val commands = ArrayList<ActionCommand>()
        for (room in rooms) {
            val state = room.computeRoomState(monitoringStationClient)
            rules.asSequence()
                .flatMap { it.trigger(state) }
                .forEach { commands.add(it) }
        }
        return commands
    }
}
