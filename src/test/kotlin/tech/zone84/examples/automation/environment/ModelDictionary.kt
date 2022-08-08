package tech.zone84.examples.automation.environment

import com.google.common.collect.ImmutableList
import com.google.common.collect.ImmutableMap
import tech.zone84.examples.automation.domain.engine.MonitoringRule
import tech.zone84.examples.automation.domain.model.Device
import tech.zone84.examples.automation.domain.model.Room
import tech.zone84.examples.automation.domain.port.RoomRepository
import tech.zone84.examples.automation.domain.port.RuleRepository

class ModelDictionary : RoomRepository, RuleRepository {
    private var roomDictionary: ImmutableMap<String, Room> = ImmutableMap.of()
    private var rules: List<MonitoringRule> = emptyList()

    fun installModel(rooms: List<Room>, rules: List<MonitoringRule>) {
        this.roomDictionary = rooms.associateBy({ it.name }) { it }.let { ImmutableMap.copyOf(it) }
        this.rules = rules
    }

    fun room(name: String): Room = roomDictionary[name] ?: throw IllegalArgumentException("No such room: '$name'")

    fun device(roomName: String, name: String): Device = room(roomName).fetchDevice(name)

    override fun fetchRooms(): List<Room> = ImmutableList.copyOf(roomDictionary.values)

    override fun fetchRules(): List<MonitoringRule> = rules
}
