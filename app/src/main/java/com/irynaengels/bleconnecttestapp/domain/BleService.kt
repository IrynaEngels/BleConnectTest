package com.irynaengels.bleconnecttestapp.domain

interface BleService {

    /**
     * Starts scanning for BLE devices.
     */
    fun startScanning()

    /**
     * Stops scanning for BLE devices.
     */
    fun stopScanning()

    /**
     * Connects to a BLE device.
     * @param macAddress The MAC address of the device to connect to.
     */
    fun connectToDevice(macAddress: String)

    /**
     * Disconnects from the currently connected BLE device.
     */
    fun disconnectDevice()

    /**
     * Reads data from a specified characteristic.
     * @param characteristicUuid The UUID of the characteristic to read from.
     */
    fun readCharacteristic(characteristicUuid: String)

    /**
     * Writes data to a specified characteristic.
     * @param characteristicUuid The UUID of the characteristic to write to.
     * @param data The data to write.
     */
    fun writeCharacteristic(characteristicUuid: String, data: ByteArray)

}
