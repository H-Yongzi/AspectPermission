package com.aspect.permission.annotation

/**
 * 校验/申请权限注释
 * <p>
 * Date: 2022-7-14
 * @param permissions 需要申请的权限列表
 * @param requestCode 请求码，默认为0
 *
 * Author: huangyong
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class PermissionRequest(vararg val permissions: String, val requestCode: Int = 0)