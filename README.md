# AspectPermission

AspectJ实现权限校验

## 使用方式

1. 在项目根目录build.gradle中添加配置

```
dependencies {
    classpath "com.hujiang.aspectjx:gradle-android-plugin-aspectjx:2.0.10"
}
```

2. 需要权限校验的module中的build.gradle中添加配置

``` 
plugins {
    id 'android-aspectjx'
}

aspectjx {
    // 需要校验权限的代码包名，可以使用app包名
    include 'com.xx.xx'
    enabled true
}

dependencies {
    implementation 'com.github.H-Yongzi:AspectPermission:1.0.0'
}
``` 

3. 权限校验

```
@PermissionRequest(permissions = [android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_CALENDAR])
private fun requestPermission() {
    // do something
}

@PermissionRequestFailed
private fun requestFailed() {
    // do something，like give tips
}
```

4. 混淆配置

```
 -keepclasseswithmembers class * {
     @com.aspect.permission.annotation.PermissionRequest <methods>;
 }

 -keepclasseswithmembers class * {
     @com.aspect.permission.annotation.PermissionRequestFailed <methods>;
 }
```
