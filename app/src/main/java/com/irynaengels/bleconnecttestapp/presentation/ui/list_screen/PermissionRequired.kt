package com.irynaengels.bleconnecttestapp.presentation.ui.list_screen


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PermissionsRequired(
    permissionsState: MultiplePermissionsState,
    onPermissionsGranted: @Composable () -> Unit,
    onPermissionsShowRationale: @Composable () -> Unit,
    onPermissionsDenied: @Composable () -> Unit,
) {
    LaunchedEffect(permissionsState) {
        permissionsState.launchMultiplePermissionRequest()
    }

    when {
        permissionsState.allPermissionsGranted -> {
            onPermissionsGranted()
        }

        permissionsState.shouldShowRationale -> {
            onPermissionsShowRationale()
        }


        else -> {
            onPermissionsDenied()
        }
    }

}
