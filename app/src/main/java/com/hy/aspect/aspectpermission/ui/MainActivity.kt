package com.hy.aspect.aspectpermission.ui

import android.Manifest
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aspect.permission.annotation.PermissionRequest
import com.aspect.permission.annotation.PermissionRequestFailed
import com.aspect.permission.aspect.PermissionDetails
import com.hy.aspect.aspectpermission.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.bt_request).setOnClickListener {
            requestPermission()
        }
    }

    @PermissionRequest(
        permissions = [Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA],
        requestCode = 1
    )
    fun requestPermission() {
        // do something
        Toast.makeText(this, "申请权限成功", Toast.LENGTH_SHORT).show()
    }

    @PermissionRequestFailed
    fun requestFailed(permissionDetail: PermissionDetails) {
        // do something，like give tips
        Toast.makeText(this, "申请权限失败", Toast.LENGTH_SHORT).show()
    }

}