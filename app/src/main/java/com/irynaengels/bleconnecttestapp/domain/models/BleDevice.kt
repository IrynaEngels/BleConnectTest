package com.irynaengels.bleconnecttestapp.domain.models

import com.polidea.rxandroidble3.RxBleDevice

data class BleDevice(
    val rxBleDevice: RxBleDevice,
    val rssi: Int
)
