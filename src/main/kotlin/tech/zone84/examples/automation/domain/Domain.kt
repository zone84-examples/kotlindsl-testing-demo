package tech.zone84.examples.automation.domain

import tech.zone84.examples.automation.domain.engine.CommandAuditor
import tech.zone84.examples.automation.domain.engine.ExecutionEngine
import tech.zone84.examples.automation.domain.port.MonitoringStationClient
import tech.zone84.examples.automation.domain.port.RoomRepository
import tech.zone84.examples.automation.domain.port.RuleRepository

class Domain(
    private val monitoringStationClient: MonitoringStationClient,
    private val roomRepository: RoomRepository,
    private val ruleRepository: RuleRepository,
    private val commandAuditor: CommandAuditor,
) {
    private val executionEngine = ExecutionEngine(monitoringStationClient, roomRepository, ruleRepository, commandAuditor)

    fun execute(steps: Int) = executionEngine.execute(steps)
}
