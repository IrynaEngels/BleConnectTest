package com.irynaengels.bleconnecttestapp.domain.models

import java.util.UUID

data class BleCharacteristicModel(val uuid: UUID, val properties: List<BleCharacteristicProperty>)

enum class BleCharacteristicProperty {
    READ, WRITE, NOTIFY
}