package tech.zone84.examples.automation.domain.model

data class Measure(
    val value: Double,
    val unit: MeasureUnit
)

enum class MeasureUnit {
    TEMP_CELSIUS,
    HUMIDITY_PERCENTAGE
}
