package com.irynaengels.bleconnecttestapp.data

import android.bluetooth.BluetoothGattCharacteristic
import com.irynaengels.bleconnecttestapp.domain.BleService
import com.irynaengels.bleconnecttestapp.domain.models.BleCharacteristicModel
import com.irynaengels.bleconnecttestapp.domain.models.BleCharacteristicProperty
import com.irynaengels.bleconnecttestapp.domain.models.BleDevice
import com.irynaengels.bleconnecttestapp.domain.models.BleServiceModel
import com.polidea.rxandroidble3.RxBleClient
import com.polidea.rxandroidble3.RxBleDevice
import com.polidea.rxandroidble3.scan.ScanFilter
import com.polidea.rxandroidble3.scan.ScanSettings
import io.reactivex.rxjava3.disposables.Disposable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject

const val BATTERY_SERVICE_UUID = "0000180F-0000-1000-8000-00805F9B34FB"

class BleServiceImpl @Inject constructor(private val rxBleClient: RxBleClient) : BleService {

    private var scanDisposable: Disposable? = null
    private var connectionDisposable: Disposable? = null
    private val _connectedDeviceFlow = MutableStateFlow<RxBleDevice?>(null)
    val connectedDeviceFlow: StateFlow<RxBleDevice?> = _connectedDeviceFlow.asStateFlow()


    private val deviceTimeoutMillis = 5000L
    private val lastSeenMap = ConcurrentHashMap<String, Long>()
    private val _visibleDevicesFlow = MutableStateFlow<List<BleDevice>>(emptyList())
    val visibleDevicesFlow: StateFlow<List<BleDevice>> = _visibleDevicesFlow.asStateFlow()

    private val _servicesAndCharacteristicsFlow = MutableStateFlow<List<BleServiceModel>>(emptyList())
    val servicesAndCharacteristicsFlow: StateFlow<List<BleServiceModel>> = _servicesAndCharacteristicsFlow.asStateFlow()


    init {
        CoroutineScope(Dispatchers.Default).launch {
            while (isActive) {
                val currentTime = System.currentTimeMillis()
                val removedDevices = mutableListOf<BleDevice>()

                lastSeenMap.entries.forEach { (macAddress, lastSeenTime) ->
                    if (currentTime - lastSeenTime > deviceTimeoutMillis) {
                        lastSeenMap.remove(macAddress)
                        _visibleDevicesFlow.value.find { device -> device.rxBleDevice.macAddress == macAddress }
                            ?.let { device ->
                                removedDevices.add(device)
                            }
                    }
                }

                if (removedDevices.isNotEmpty()) {
                    _visibleDevicesFlow.value = _visibleDevicesFlow.value - removedDevices
                }

                delay(1000)
            }
        }
    }


    override fun startScanning() {
        val scanSettings = ScanSettings.Builder()
            .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
            .build()

        val scanFilters: Array<ScanFilter> = arrayOf()

        scanDisposable?.dispose()

        scanDisposable = rxBleClient.scanBleDevices(scanSettings, *scanFilters)
            .subscribe(
                { scanResult ->
                    val rxBleDevice = scanResult.bleDevice
                    val rssi = scanResult.rssi
                    val macAddress = rxBleDevice.macAddress
                    lastSeenMap[macAddress] = System.currentTimeMillis()

                    val newDevice = BleDevice(rxBleDevice, rssi)
                    val currentDevices = _visibleDevicesFlow.value
                    if (currentDevices.none { it.rxBleDevice.macAddress == macAddress }) {
                        _visibleDevicesFlow.value = currentDevices + newDevice
                    } else {

                        _visibleDevicesFlow.value = currentDevices.map { device ->
                            if (device.rxBleDevice.macAddress == macAddress) newDevice else device
                        }
                    }
                },
                { throwable ->

                }
            )
    }

    override fun stopScanning() {
        scanDisposable?.dispose()
    }

    override fun connectToDevice(macAddress: String) {
        _connectedDeviceFlow.value = rxBleClient.getBleDevice(macAddress)
        connectionDisposable?.dispose()

        _connectedDeviceFlow.value?.let { device ->
            connectionDisposable = device.establishConnection(false)
                .flatMap { rxBleConnection ->
                    rxBleConnection.discoverServices().toObservable()
                }
                .subscribe(
                    { rxBleDeviceServices ->
                        val bleServices = rxBleDeviceServices.bluetoothGattServices.map { service ->
                            BleServiceModel(
                                uuid = service.uuid,
                                characteristics = service.characteristics.map { characteristic ->
                                    BleCharacteristicModel(
                                        uuid = characteristic.uuid,
                                        properties = characteristic.properties.toBleCharacteristicProperties()
                                    )
                                }
                            )
                        }
                        _servicesAndCharacteristicsFlow.value = bleServices
                    },
                    { throwable ->

                    }
                )
        } ?: run {

        }
    }


    override fun disconnectDevice() {
        connectionDisposable?.dispose()
        _connectedDeviceFlow.value = null
    }

    override fun readCharacteristic(characteristicUuid: String) {
        TODO("Not yet implemented")
    }

    override fun writeCharacteristic(characteristicUuid: String, data: ByteArray) {
        TODO("Not yet implemented")
    }

    private fun Int.toBleCharacteristicProperties(): List<BleCharacteristicProperty> {
        val propertiesList = mutableListOf<BleCharacteristicProperty>()
        if (this and BluetoothGattCharacteristic.PROPERTY_READ != 0) {
            propertiesList.add(BleCharacteristicProperty.READ)
        }
        if (this and BluetoothGattCharacteristic.PROPERTY_WRITE != 0) {
            propertiesList.add(BleCharacteristicProperty.WRITE)
        }
        if (this and BluetoothGattCharacteristic.PROPERTY_NOTIFY != 0) {
            propertiesList.add(BleCharacteristicProperty.NOTIFY)
        }
        return propertiesList
    }


}
