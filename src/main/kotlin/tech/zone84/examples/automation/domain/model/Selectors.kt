package tech.zone84.examples.automation.domain.model

fun interface Selector<T> {
    fun select(state: RoomState): Sequence<T>
}

class AllDevicesSelector : Selector<Device> {
    override fun select(state: RoomState): Sequence<Device> = state
        .room
        .devices
        .asSequence()
}

class OnlyDevices(val devices: Set<String>) : Selector<Device> {
    override fun select(state: RoomState): Sequence<Device> = state
        .room
        .devices
        .filter { it.name in devices }
        .asSequence()
}

class AllSensorsSelector : Selector<Sensor> {
    override fun select(state: RoomState): Sequence<Sensor> = state
        .room
        .stations
        .asSequence()
        .flatMap { it.sensors }
}

object Selectors {
    fun allDevices(): Selector<Device> = AllDevicesSelector()
    fun allSensors(): Selector<Sensor> = AllSensorsSelector()

    fun onlyDevices(vararg devices: String) = OnlyDevices(devices.toSet())
}
