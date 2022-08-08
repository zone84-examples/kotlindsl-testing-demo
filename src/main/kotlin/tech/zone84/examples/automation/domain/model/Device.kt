package tech.zone84.examples.automation.domain.model

import mu.KotlinLogging

sealed interface Device {
    val name: String
    val enabled: Boolean

    fun turnOn()
    fun turnOff()
}

data class FanDevice(override val name: String) : Device {
    override var enabled: Boolean = false
        private set

    override fun turnOn() {
        logger.info { "Fan '$name' turns on" }
        enabled = true
    }

    override fun turnOff() {
        logger.info { "Fan '$name' turns off" }
        enabled = false
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}

data class HeaterDevice(override val name: String) : Device {
    override var enabled: Boolean = false
        private set

    override fun turnOn() {
        logger.info { "Heater '$name' turns on" }
        enabled = true
    }

    override fun turnOff() {
        logger.info { "Heater '$name' turns off" }
        enabled = false
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}

data class DehumidifierDevice(override val name: String) : Device {
    override var enabled: Boolean = false
        private set

    override fun turnOn() {
        logger.info { "Dehumidifier '$name' turns on" }
        enabled = true
    }

    override fun turnOff() {
        logger.info { "Dehumidifier '$name' turns off" }
        enabled = false
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
