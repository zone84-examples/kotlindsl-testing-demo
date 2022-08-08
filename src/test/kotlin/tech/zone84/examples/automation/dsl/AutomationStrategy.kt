package tech.zone84.examples.automation.dsl

import tech.zone84.examples.automation.domain.engine.MonitoringRule

interface AutomationStrategy {
    fun process(rooms: List<RoomDefinition>, rules: List<MonitoringRule>)
}
