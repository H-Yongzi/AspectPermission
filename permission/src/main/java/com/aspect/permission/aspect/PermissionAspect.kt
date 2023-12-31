package com.aspect.permission.aspect

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.aspect.permission.annotation.PermissionRequest
import com.aspect.permission.annotation.PermissionRequestFailed
import com.aspect.permission.utils.TAG
import com.aspect.permission.utils.application
import com.aspect.permission.utils.awaitStartActivityAndCreate
import com.aspect.permission.utils.getTopActivity
import com.aspect.permission.utils.hasSelfPermissions
import com.aspect.permission.utils.requestPermissionsForResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut

/**
 * Aspect切面
 * <p>
 * Date: 2022-7-14
 *
 * Author: huangyong
 */
@Aspect
class PermissionAspect {

    @Pointcut("execution(@com.aspect.permission.annotation.PermissionRequest * *(..))" + " && @annotation(permissionRequest)")
    fun requestPermissionMethod(permissionRequest: PermissionRequest) {
    }

    @Around("requestPermissionMethod(permissionRequest)")
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint, permissionRequest: PermissionRequest) {
        if (hasSelfPermissions(application, *permissionRequest.permissions)) {
            try {
                joinPoint.proceed()
            } catch (throwable: Throwable) {
                Log.d(TAG, throwable.localizedMessage.orEmpty())
            }
            return
        }
        requestPermissions(joinPoint, permissionRequest)
    }

    /**
     * Desc: 申请权限
     * <p>
     * Author: huangyong
     * Date: 2022/7/14
     */
    private fun requestPermissions(joinPoint: ProceedingJoinPoint, permissionRequest: PermissionRequest) {
        MainScope().launch(Dispatchers.Main.immediate) {
            // 优先获取当前activity
            var activity = findActivityFromContext(joinPoint.`this`)
            // 如果获取不到当前activity，则启动一个透明Activity
            if (activity == null) {
                // 启动activity，onCreate之后返回activity实例
                activity = awaitStartActivityAndCreate(application, PermissionRequestActivity::class.java)
            }
            try {
                // 申请权限，返回申请结果
                val result =
                    activity.requestPermissionsForResult(permissionRequest.permissions as Array<String>, permissionRequest.requestCode)
                if (activity is PermissionRequestActivity) {
                    activity.finish()
                }
                // 如果没有权限被拒绝，则权限申请通过
                if (result.deniedPermissions.isEmpty()) {
                    joinPoint.proceed()
                } else {
                    onDenied(joinPoint, result)
                }
            } catch (throwable: Throwable) {
                Log.e(TAG, throwable.localizedMessage.orEmpty())
            }
        }
    }

    /**
     * Desc: 权限被拒绝，反射调用[PermissionRequestFailed]注解的方法，支持无参或只有一个[PermissionDetail]参数
     * <p>
     * Author: huangyong
     * Date: 2022/7/14
     */
    private fun onDenied(joinPoint: ProceedingJoinPoint, permissionDetail: PermissionDetails) {
        // 通过joinPoint.`this`获取joinPoint所在的对象
        val cls: Class<*> = joinPoint.`this`.javaClass
        val methods = cls.declaredMethods
        if (methods.isEmpty()) return
        methods.firstOrNull {
            it.isAnnotationPresent(PermissionRequestFailed::class.java)
        }?.apply {
            isAccessible = true
            val types = parameterTypes
            if (types.isEmpty()) {
                invoke(joinPoint.`this`)
            } else if (types.size == 1) {
                invoke(joinPoint.`this`, permissionDetail)
            }
        }
    }

    /**
     * Desc: 从切面上下文中获取当前Activity
     * <p>
     * Author: huangyong
     * Date: 2022/7/14
     */
    private fun findActivityFromContext(any: Any): FragmentActivity? {
        if (any is Fragment) {
            val activity = any.activity
            if (activity != null && !activity.isDestroyed) {
                return activity
            }
        }
        if (any is FragmentActivity) {
            return any
        }
        val curActivity = getTopActivity()
        if (curActivity is FragmentActivity) {
            return curActivity
        }
        return null
    }
}