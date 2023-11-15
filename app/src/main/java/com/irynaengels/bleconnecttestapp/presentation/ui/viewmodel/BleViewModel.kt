package com.irynaengels.bleconnecttestapp.presentation.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irynaengels.bleconnecttestapp.data.BleServiceImpl
import com.irynaengels.bleconnecttestapp.domain.models.BleDevice
import com.irynaengels.bleconnecttestapp.domain.models.BleServiceModel
import com.polidea.rxandroidble3.RxBleDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BleViewModel @Inject constructor(
    private val bleService: BleServiceImpl
) : ViewModel() {

    val visibleDevices: StateFlow<List<BleDevice>> = bleService.visibleDevicesFlow

    val connectedDevice: StateFlow<RxBleDevice?> = bleService.connectedDeviceFlow

    val servicesAndCharacteristics: StateFlow<List<BleServiceModel>> = bleService.servicesAndCharacteristicsFlow


    fun startScanning() {
        viewModelScope.launch {
            bleService.startScanning()
        }
    }

    fun stopScanning() {
        bleService.stopScanning()
    }

    fun connectToDevice(macAddress: String) {
        viewModelScope.launch {
            bleService.connectToDevice(macAddress)
        }
    }

    fun disconnectDevice() {
        bleService.disconnectDevice()
    }

    override fun onCleared() {
        super.onCleared()
        bleService.stopScanning()
    }
}
