package com.irynaengels.bleconnecttestapp.presentation.ui.list_screen

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.irynaengels.bleconnecttestapp.R
import com.irynaengels.bleconnecttestapp.presentation.LocalBleViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.irynaengels.bleconnecttestapp.presentation.ui.navigation.Destinations
import com.irynaengels.bleconnecttestapp.presentation.ui.theme.PurpleCustom

@Composable
fun DeviceListWithPermissionsScreen(navController: NavController) {
    var showBleScreen by remember { mutableStateOf(false) }

    if (showBleScreen) {
        DeviceListScreen(navController)
    } else {
        PermissionsRequired(onPermissionsGranted = { showBleScreen = true })
    }
}

@Composable
fun DeviceListScreen(navController: NavController) {
    val viewModel = LocalBleViewModel.current
    val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()
    val startBluetoothIntent = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) {}

    val devices = viewModel.visibleDevices.collectAsState(initial = emptyList())
    var isScanning by remember { mutableStateOf(false) }

    val connectedDevice by viewModel.connectedDevice.collectAsState()
    if (connectedDevice != null){
        navController.navigate(Destinations.DEVICE_DETAIL)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleCustom)
    ) {
        Image(
            painter = painterResource(id = R.drawable.frame_1),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            if (isScanning) {
                Text(
                    text = "Searching for your device",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = "Click on the device to connect",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
            LazyColumn(
                modifier = Modifier.weight(1f)
            ) {
                items(devices.value) { device ->
                    DeviceItem(device = device, onClick = {
                        viewModel.connectToDevice(device.rxBleDevice.macAddress)
                    })
                }
            }

            if (!isScanning) {
                Button(
                    onClick = {
                        if (bluetoothAdapter?.isEnabled == true) {
                            isScanning = true
                            viewModel.startScanning()
                        } else {
                            startBluetoothIntent.launch(Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE))
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                ) {
                    Text("Start Scan")
                }
            }
        }
    }
}
