package com.irynaengels.bleconnecttestapp.presentation.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.irynaengels.bleconnecttestapp.presentation.ui.detail_screen.DeviceDetailScreen
import com.irynaengels.bleconnecttestapp.presentation.ui.list_screen.DeviceListWithPermissionsScreen

@Composable
fun BleConnectNavGraph(navController: NavHostController) {
    NavHost(navController, Destinations.DEVICE_LIST) {
        composable(Destinations.DEVICE_LIST) {
            DeviceListWithPermissionsScreen(navController)
        }

        composable(Destinations.DEVICE_DETAIL) {
            DeviceDetailScreen()
        }
    }
}

object Destinations {
    const val DEVICE_LIST = "device_list"
    const val DEVICE_DETAIL = "device_detail"
}