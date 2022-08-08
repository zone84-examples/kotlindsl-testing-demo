package tech.zone84.examples.automation.environment

import com.google.common.collect.ImmutableList
import tech.zone84.examples.automation.domain.engine.ActionCommand
import tech.zone84.examples.automation.domain.engine.CommandAuditor

class RecordingCommandAuditor : CommandAuditor {
    private val recording: MutableList<ActionCommand> = ArrayList()

    fun fetchRecordedCommands(): List<ActionCommand> = ImmutableList.copyOf(recording)

    override fun onCommandsProduced(commands: List<ActionCommand>) {
        recording.addAll(commands)
    }
}
