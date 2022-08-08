package tech.zone84.examples.automation.domain.engine

import tech.zone84.examples.automation.domain.port.MonitoringStationClient
import tech.zone84.examples.automation.domain.port.RoomRepository
import tech.zone84.examples.automation.domain.port.RuleRepository

class ExecutionEngine(
    private val monitoringStationClient: MonitoringStationClient,
    private val roomRepository: RoomRepository,
    private val ruleRepository: RuleRepository,
    private val auditor: CommandAuditor
) {
    fun execute(steps: Int) {
        val rules = ruleRepository.fetchRules()
        val rooms = roomRepository.fetchRooms()

        val stepExecutor = StepExecutor(rooms, rules)
        for (i in 1 .. steps) {
            val commands = stepExecutor.executeStep(monitoringStationClient)
            auditor.onCommandsProduced(commands)
            for (command in commands) {
                command.execute()
            }
        }
    }
}
