package com.aspect.permission.utils

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.collection.SimpleArrayMap
import androidx.core.app.ActivityCompat

private var MIN_SDK_PERMISSIONS = SimpleArrayMap<String, Int>().apply {
    put("com.android.voicemail.permission.ADD_VOICEMAIL", 14)
    put("android.permission.BODY_SENSORS", 20)
    put("android.permission.READ_CALL_LOG", 16)
    put("android.permission.READ_EXTERNAL_STORAGE", 16)
    put("android.permission.USE_SIP", 9)
    put("android.permission.WRITE_CALL_LOG", 16)
    put("android.permission.SYSTEM_ALERT_WINDOW", 23)
    put("android.permission.WRITE_SETTINGS", 23)
}

/**
 * 判断是否所有权限都同意了，都同意返回true 否则返回false
 * <p>
 * Date:  2022-7-14 09:57
 * Author: huangyong
 * @param context
 * @param permissions 权限列表
 * @return Boolean true -> 所有权限都申请通过
 */
fun hasSelfPermissions(context: Context, vararg permissions: String): Boolean {
    for (permission in permissions) {
        if (permissionExists(permission) && !hasSelfPermission(context, permission)) {
            return false
        }
    }
    return true
}

/**
 * 判断单个权限是否同意
 * <p>
 * Date:  2022-7-14 09:51
 * Author: huangyong
 * @param context
 * @param permission
 * @return Boolean
 */
private fun hasSelfPermission(context: Context, permission: String): Boolean {
    return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
}

/**
 * 判断权限是否存在
 * <p>
 * Date:  2022-7-14 09:53
 * Author: huangyong
 * @param permission
 * @return Boolean
 */
private fun permissionExists(permission: String): Boolean {
    val minVersion = MIN_SDK_PERMISSIONS[permission]
    return minVersion == null || Build.VERSION.SDK_INT >= minVersion
}