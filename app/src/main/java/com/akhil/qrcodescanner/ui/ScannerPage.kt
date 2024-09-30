package com.akhil.qrcodescanner.ui

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.android.gms.common.moduleinstall.ModuleInstall
import com.google.android.gms.common.moduleinstall.ModuleInstallRequest
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.codescanner.GmsBarcodeScanner
import com.google.mlkit.vision.codescanner.GmsBarcodeScannerOptions
import com.google.mlkit.vision.codescanner.GmsBarcodeScanning

private var isScannerInstalled = false
private lateinit var scanner: GmsBarcodeScanner

@Composable
fun ScannerPage() {
    var scannedValue by remember { mutableStateOf("") }
    var onScanClick by remember { mutableStateOf(false) }
    val mContext = LocalContext.current

    LaunchedEffect(Unit) {
        installGoogleScanner(mContext)
        initVars(mContext)
    }

    LaunchedEffect(onScanClick) {
        if(onScanClick){
            if (isScannerInstalled) {
                scanner.startScan().addOnSuccessListener {
                    val result = it.rawValue
                    result?.let {
                        scannedValue = it
                    }
                }.addOnCanceledListener {
                    Toast.makeText(mContext, "Cancelled", Toast.LENGTH_SHORT).show()

                }
                    .addOnFailureListener {
                        Toast.makeText(mContext, it.message, Toast.LENGTH_SHORT).show()

                    }
            } else {
                Toast.makeText(mContext, "Please try again...", Toast.LENGTH_SHORT).show()
            }
        }
        onScanClick = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 32.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.align(Alignment.BottomCenter)
        ) {
            Text(
                text = "Scanned Value: $scannedValue",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            Button(
                onClick = {
                    onScanClick = true
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Scan QR")
            }
        }
    }
}
private fun installGoogleScanner(mContext : Context) {
    val moduleInstall = ModuleInstall.getClient(mContext)
    val moduleInstallRequest = ModuleInstallRequest.newBuilder()
        .addApi(GmsBarcodeScanning.getClient(mContext))
        .build()

    moduleInstall.installModules(moduleInstallRequest).addOnSuccessListener {
        isScannerInstalled = true
    }.addOnFailureListener {
        isScannerInstalled = false
        Toast.makeText(mContext, it.message, Toast.LENGTH_SHORT).show()
    }
}
private fun initVars(mContext : Context) {
    val options = initializeGoogleScanner()
    scanner = GmsBarcodeScanning.getClient(mContext, options)
}
private fun initializeGoogleScanner(): GmsBarcodeScannerOptions {
    return GmsBarcodeScannerOptions.Builder().setBarcodeFormats(Barcode.FORMAT_QR_CODE)
        .enableAutoZoom().build()
}