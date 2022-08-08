package tech.zone84.examples.automation.domain.port

import tech.zone84.examples.automation.domain.model.Room

interface RoomRepository {
    fun fetchRooms(): List<Room>
}