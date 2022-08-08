package tech.zone84.examples.automation.domain.engine

interface CommandAuditor {
    fun onCommandsProduced(commands: List<ActionCommand>)
}