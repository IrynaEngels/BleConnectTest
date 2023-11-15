package com.irynaengels.bleconnecttestapp.domain.models

import java.util.UUID

data class BleServiceModel(val uuid: UUID, val characteristics: List<BleCharacteristicModel>)
