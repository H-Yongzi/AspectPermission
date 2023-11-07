package com.aspect.permission.aspect

/**
 * 权限申请结果明细
 * <p>
 * Date: 2022-7-14
 * @param requestCode 权限请求码
 * @param grantedPermissions 授予权限列表
 * @param deniedPermissions 拒绝权限列表
 * @param rejectRemind 拒绝权限且勾选了不再提示
 *
 * Author: huangyong
 */
data class PermissionDetails(
    val requestCode: Int,
    val grantedPermissions: List<String>,
    val deniedPermissions: List<String>,
    val rejectRemind: Boolean
)
