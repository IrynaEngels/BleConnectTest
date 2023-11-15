package com.irynaengels.bleconnecttestapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.irynaengels.bleconnecttestapp.presentation.ui.navigation.BleConnectNavGraph
import com.irynaengels.bleconnecttestapp.presentation.ui.theme.BleConnectTestAppTheme
import com.irynaengels.bleconnecttestapp.presentation.ui.viewmodel.BleViewModel
import dagger.hilt.android.AndroidEntryPoint

val LocalBleViewModel =
    compositionLocalOf<BleViewModel> { error("No BLE view model found!") }

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val bleViewModel: BleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BleConnectTestAppTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CompositionLocalProvider(
                        LocalBleViewModel provides bleViewModel
                    ){
                        BleConnectNavGraph(navController)
                    }
                }
            }
        }
    }
}