package tech.zone84.examples.automation.domain

import io.kotest.core.spec.IsolationMode
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.collections.shouldContainExactly
import tech.zone84.examples.automation.domain.TestData.FAN1_DEVICE
import tech.zone84.examples.automation.domain.TestData.FAN2_DEVICE
import tech.zone84.examples.automation.domain.TestData.FAN3_DEVICE
import tech.zone84.examples.automation.domain.TestData.HEATER1_DEVICE
import tech.zone84.examples.automation.domain.TestData.MAIN_HALL
import tech.zone84.examples.automation.domain.TestData.ROOM_1
import tech.zone84.examples.automation.domain.TestData.ROOM_2
import tech.zone84.examples.automation.domain.engine.DeviceTurnOffCommand
import tech.zone84.examples.automation.domain.engine.DeviceTurnOnCommand
import tech.zone84.examples.automation.domain.engine.MaxTemperature
import tech.zone84.examples.automation.domain.engine.MinTemperature
import tech.zone84.examples.automation.domain.model.Selectors.allDevices
import tech.zone84.examples.automation.domain.model.Selectors.allSensors
import tech.zone84.examples.automation.domain.model.Selectors.onlyDevices
import tech.zone84.examples.automation.environment.DomainOperations

class AutomationSpec : ShouldSpec({
    isolationMode = IsolationMode.InstancePerTest

    val domain = DomainOperations()

    should("turn on fans in room 1 when the temperature exceeds the threshold") {
        // given
        domain.thereIsUseCase {
            room(name = ROOM_1) {
                monitoringStation(name = "MS1", address = "https://ms1.test.local") {
                    temperatureSensor(name = "T1", address = 1) {
                        readings(23, 24, 25, 26)
                    }
                }
                fan(name = FAN1_DEVICE)
            }
            rules(
                MaxTemperature(threshold = 25.5, sensors = allSensors(), devices = allDevices())
            )
        }

        // when
        domain.actual.execute(steps = 4)

        // then
        with(domain.dictionary) {
            domain.capturedCommands shouldContainExactly listOf(
                DeviceTurnOnCommand(room(ROOM_1), device(ROOM_1, FAN1_DEVICE))
            )
        }
    }

    should("turn on and off fans in room 1 when the temperature exceeds the threshold and then drops") {
        // given
        domain.thereIsUseCase {
            room(name = ROOM_1) {
                monitoringStation(name = "MS1", address = "https://ms1.test.local") {
                    temperatureSensor(name = "T1", address = 1) {
                        readings(23, 24, 25, 26, 25, 24)
                    }
                }
                fan(name = FAN1_DEVICE)
            }
            rules(
                MaxTemperature(threshold = 25.5, sensors = allSensors(), devices = allDevices())
            )
        }

        // when
        domain.actual.execute(steps = 6)

        // then
        with(domain.dictionary) {
            domain.capturedCommands shouldContainExactly listOf(
                DeviceTurnOnCommand(room(ROOM_1), device(ROOM_1, FAN1_DEVICE)),
                DeviceTurnOffCommand(room(ROOM_1), device(ROOM_1, FAN1_DEVICE))
            )
        }
    }

    should("control temperatures in two rooms independently") {
        // given
        domain.thereIsUseCase {
            room(name = ROOM_1) {
                monitoringStation(name = "MS1", address = "https://ms1.test.local") {
                    temperatureSensor(name = "T1", address = 1) {
                        readings(23, 24, 25, 26, 27, 27, 27, 26, 25, 24, 23, 23, 23, 23, 23, 23, 23)
                    }
                }
                fan(name = FAN1_DEVICE)
                fan(name = FAN2_DEVICE)
            }
            room(name = ROOM_2) {
                monitoringStation(name = "MS2", address = "https://ms2.test.local") {
                    temperatureSensor(name = "T2", address = 1) {
                        readings(23, 23, 23, 23, 23, 23, 23, 23, 23, 23, 24, 25, 26, 27, 26, 25, 24)
                    }
                }
                fan(name = FAN3_DEVICE)
            }
            rules(
                MaxTemperature(threshold = 25.5, sensors = allSensors(), devices = allDevices())
            )
        }

        // when
        domain.actual.execute(steps = 17)

        // then
        with(domain.dictionary) {
            domain.capturedCommands shouldContainExactly listOf(
                DeviceTurnOnCommand(room(ROOM_1), device(ROOM_1, FAN1_DEVICE)),
                DeviceTurnOnCommand(room(ROOM_1), device(ROOM_1, FAN2_DEVICE)),
                DeviceTurnOffCommand(room(ROOM_1), device(ROOM_1, FAN1_DEVICE)),
                DeviceTurnOffCommand(room(ROOM_1), device(ROOM_1, FAN2_DEVICE)),
                DeviceTurnOnCommand(room(ROOM_2), device(ROOM_2, FAN3_DEVICE)),
                DeviceTurnOffCommand(room(ROOM_2), device(ROOM_2, FAN3_DEVICE))
            )
        }
    }

    should("control multiple devices with several rules") {
        // given
        domain.thereIsUseCase {
            room(name = MAIN_HALL) {
                monitoringStation(name = "MS1", address = "https://ms1.test.local") {
                    temperatureSensor(name = "T1", address = 1) {
                        readings(23, 24, 25, 26, 27, 23, 28, 30, 31, 30, 29, 28, 27, 26, 24, 22, 19, 18, 19, 21, 22)
                    }
                    temperatureSensor(name = "T2", address = 2) {
                        readings(23, 23, 24, 24, 26, 26, 29, 30, 31, 30, 29, 28, 27, 26, 24, 22, 18, 16, 19, 20, 21)
                    }
                }
                fan(name = FAN1_DEVICE)
                fan(name = FAN2_DEVICE)
                heater(name = HEATER1_DEVICE)
            }
            rules(
                MaxTemperature(threshold = 25.5, sensors = allSensors(), devices = onlyDevices(FAN1_DEVICE)),
                MaxTemperature(threshold = 30.5, sensors = allSensors(), devices = onlyDevices(FAN2_DEVICE)),
                MinTemperature(threshold = 20.5, sensors = allSensors(), devices = onlyDevices(HEATER1_DEVICE))
            )
        }

        // when
        domain.actual.execute(steps = 21)

        // then
        with(domain.dictionary) {
            domain.capturedCommands shouldContainExactly listOf(
                DeviceTurnOnCommand(room(MAIN_HALL), device(MAIN_HALL, FAN1_DEVICE)),
                DeviceTurnOffCommand(room(MAIN_HALL), device(MAIN_HALL, FAN1_DEVICE)),
                DeviceTurnOnCommand(room(MAIN_HALL), device(MAIN_HALL, FAN1_DEVICE)),
                DeviceTurnOnCommand(room(MAIN_HALL), device(MAIN_HALL, FAN2_DEVICE)),
                DeviceTurnOffCommand(room(MAIN_HALL), device(MAIN_HALL, FAN2_DEVICE)),
                DeviceTurnOffCommand(room(MAIN_HALL), device(MAIN_HALL, FAN1_DEVICE)),
                DeviceTurnOnCommand(room(MAIN_HALL), device(MAIN_HALL, HEATER1_DEVICE)),
                DeviceTurnOffCommand(room(MAIN_HALL), device(MAIN_HALL, HEATER1_DEVICE))
            )
        }
    }
})
