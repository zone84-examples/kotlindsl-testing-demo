package tech.zone84.examples.automation.environment

import tech.zone84.examples.automation.domain.Domain
import tech.zone84.examples.automation.domain.engine.ActionCommand
import tech.zone84.examples.automation.dsl.AutomationDefinition
import tech.zone84.examples.automation.dsl.AutomationDsl
import tech.zone84.examples.automation.dsl.UnitTestAutomationStrategy

class DomainOperations {
    val dictionary = ModelDictionary()
    val monitoringStationClient = InMemoryMonitoringStationClient()
    val auditor = RecordingCommandAuditor()

    val capturedCommands: List<ActionCommand>
        get() = auditor.fetchRecordedCommands()

    val actual = Domain(
        monitoringStationClient = monitoringStationClient,
        roomRepository = dictionary,
        ruleRepository = dictionary,
        commandAuditor = auditor
    )

    fun thereIsUseCase(dsl: AutomationDsl.() -> Unit) {
        val definition = AutomationDefinition()
        definition.apply(dsl)
        definition.installWith(UnitTestAutomationStrategy(dictionary, monitoringStationClient))
    }
}
