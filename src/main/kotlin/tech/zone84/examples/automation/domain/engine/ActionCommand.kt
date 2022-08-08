package tech.zone84.examples.automation.domain.engine

import tech.zone84.examples.automation.domain.model.Device
import tech.zone84.examples.automation.domain.model.Room

sealed interface ActionCommand {
    val room: Room
    val target: Device

    fun execute()
}

data class DeviceTurnOnCommand(
    override val room: Room,
    override val target: Device
) : ActionCommand {
    constructor(room: Room, target: String) : this(room, room.fetchDevice(target))

    override fun execute() {
        target.turnOn()
    }
}

data class DeviceTurnOffCommand(
    override val room: Room,
    override val target: Device
) : ActionCommand {
    override fun execute() {
        target.turnOff()
    }
}
