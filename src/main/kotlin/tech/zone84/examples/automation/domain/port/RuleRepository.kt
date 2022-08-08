package tech.zone84.examples.automation.domain.port

import tech.zone84.examples.automation.domain.engine.MonitoringRule

interface RuleRepository {
    fun fetchRules(): List<MonitoringRule>
}
