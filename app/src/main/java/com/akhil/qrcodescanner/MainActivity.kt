package com.akhil.qrcodescanner

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.akhil.qrcodescanner.ui.ScannerPage
import com.akhil.qrcodescanner.ui.theme.QrCodeScannerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QrCodeScannerTheme {
                ScannerPage()
            }
        }
    }
}