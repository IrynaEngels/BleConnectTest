package com.irynaengels.bleconnecttestapp.presentation.ui.list_screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.irynaengels.bleconnecttestapp.presentation.LocalBleViewModel
import com.irynaengels.bleconnecttestapp.presentation.ui.theme.PurpleCustomLight

@Composable
fun DetailItem(device: String) {
    val viewModel = LocalBleViewModel.current
    val servicesAndCharacteristics by viewModel.servicesAndCharacteristics.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = PurpleCustomLight)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = device,
                fontSize = 18.sp,
                color = Color.Black
            )

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                LazyColumn {
                    servicesAndCharacteristics.forEach { service ->
                        item {
                            Text(
                                text = "Service UUID: ${service.uuid}",
                                fontSize = 16.sp,
                                color = Color.Black
                            )
                            service.characteristics.forEach { characteristic ->
                                Text(
                                    text = "Characteristic UUID: ${characteristic.uuid} - Properties: ${characteristic.properties.joinToString()}",
                                    fontSize = 14.sp,
                                    color = Color.DarkGray
                                )
                            }
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }

}
