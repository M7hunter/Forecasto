package com.m7.forecasto.util.handlers

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PermissionHandler @Inject constructor(@ApplicationContext val context: Context) {

    fun isGranted(vararg permissions: String) =
        permissions.all {
            ActivityCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }

    fun reqPermissions(activity: Activity, permissions: Array<String>, requestCode: Int) =
        requestPermissions(activity, permissions, requestCode)
}

