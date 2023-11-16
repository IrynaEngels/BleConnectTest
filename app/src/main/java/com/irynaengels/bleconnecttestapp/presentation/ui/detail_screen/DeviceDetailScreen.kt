package com.irynaengels.bleconnecttestapp.presentation.ui.detail_screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.irynaengels.bleconnecttestapp.R
import com.irynaengels.bleconnecttestapp.presentation.LocalBleViewModel
import com.irynaengels.bleconnecttestapp.presentation.ui.list_screen.DetailItem
import com.irynaengels.bleconnecttestapp.presentation.ui.theme.PurpleCustom
import com.irynaengels.bleconnecttestapp.presentation.ui.theme.PurpleCustomLight

@Composable
fun DeviceDetailScreen() {
    val viewModel = LocalBleViewModel.current
    val connectedDevice by viewModel.connectedDevice.collectAsState()

    var showDetails by remember { mutableStateOf(false) }

    BackHandler {
        if (showDetails){
            showDetails = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PurpleCustom)
    ) {

        Image(
            painter = painterResource(id = R.drawable.frame_2),
            contentDescription = "Background Image",
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(horizontal = 16.dp)
        ) {


            Spacer(modifier = Modifier.height(8.dp))

            if (connectedDevice != null) {
                if (!showDetails) {
                    Text(
                        text = "Your device is connected",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )

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
                                text = connectedDevice!!.name ?: connectedDevice!!.macAddress,
                                fontSize = 18.sp,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                } else {
                    DetailItem(device = connectedDevice!!.name ?: connectedDevice!!.macAddress)
                }

            }
        }
        if (!showDetails) {
            Button(
                onClick = { showDetails = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                    .padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "Show Services",
                    fontSize = 18.sp,
                    color = Color.White
                )
            }
        }
    }
}


