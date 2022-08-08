package tech.zone84.examples.automation.domain.model

sealed interface Sensor {
    val name: String
    val address: Int
}

data class TemperatureSensor(
    override val name: String,
    override val address: Int
) : Sensor {

}

data class HumiditySensor(
    override val name: String,
    override val address: Int
) : Sensor
