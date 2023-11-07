package com.aspect.permission.annotation

/**
 * 权限校验失败注解
 * <p>
 * Date: 2022-7-14
 *
 * Author: huangyong
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class PermissionRequestFailed()