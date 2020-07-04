package com.example.privacy

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Build
import android.telecom.TelecomManager
import android.util.Log
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.content.PermissionChecker
import androidx.core.net.toUri
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugins.GeneratedPluginRegistrant

import org.jetbrains.anko.doAsync
import java.net.UnknownHostException


class MainActivity : FlutterActivity() {
    override fun onStart() {
        super.onStart()
        checkDefaultDialer()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            REQUEST_CODE_SET_DEFAULT_DIALER -> checkSetDefaultDialerResult(resultCode)
        }
    }

    private var type: Int = 0

    companion object {
        private const val REQUEST_CODE_SET_DEFAULT_DIALER = 123
        const val CHANNEL = "flutter_native_channel"
        const val Call_Method = "makeCall"
        

        var phoneNumber = ""
        
    }

    override fun configureFlutterEngine(@NonNull flutterEngine: FlutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);

        MethodChannel(flutterEngine.dartExecutor, CHANNEL).setMethodCallHandler { flutterMethod, result ->
            if (flutterMethod.method == Call_Method) {
                val argument = flutterMethod.arguments<Map<String, Any>>()
                phoneNumber = (argument["phoneNumber"] as String?).toString()
                makeCall()
            }
            else {
                result.notImplemented()
            }
        }
    }

    ///make call function
    private fun makeCall() {
        if (PermissionChecker.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PermissionChecker.PERMISSION_GRANTED) {
            val uri = "tel:${phoneNumber}".toUri()
            startActivity(Intent(Intent.ACTION_CALL, uri))
        }

    }

    private fun checkDefaultDialer() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return

        val telecomManager = getSystemService(TELECOM_SERVICE) as TelecomManager
        val isAlreadyDefaultDialer = packageName == telecomManager.defaultDialerPackage
        if (isAlreadyDefaultDialer) return

        val intent = Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER)
                .putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, packageName)
        startActivityForResult(intent, REQUEST_CODE_SET_DEFAULT_DIALER)
    }

    private fun checkSetDefaultDialerResult(resultCode: Int) {
        val message = when (resultCode) {
            RESULT_OK -> "User accepted request to become default dialer"
            RESULT_CANCELED -> "User declined request to become default dialer"
            else -> "Unexpected result code $resultCode"
        }

        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}