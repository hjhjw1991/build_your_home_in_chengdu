好记性不如烂笔头, 每次自己写processor都会被坑一下. 于是干脆就随手记录
不使用google的AutoService, 因为不够优雅. 

# 1. 利用Java原本的机制自己实现一个注解处理器

## 告诉JVM需要处理annotation processor

1. 在工程里定义一个Processor. 
一个注解处理器所在的模块, 是一个纯粹的Java模块, 不会引用任何Android SDK的东西包括gradle插件. 
代码里定义processor, 如下

```kotlin
@SupportedAnnotationTypes(SERVICE_SPI, SERVICE_IMPL)
class ServiceProcessor: AbstractProcessor() {}
```

可以通过JavaCompileOptions给Processor传递参数, 参考 :app 的配置

工程里配置该 module 为Java module

```gradle
apply plugin: 'java-library' // 这是个Java module
apply plugin: 'kotlin' // 使用kotlin语言
apply plugin: 'kotlin-kapt' // 使用kotlin 注解处理器

dependencies {
}

sourceCompatibility = "1.8" // 源码用xxx版本的Java语法
targetCompatibility = "1.8" // 编译出的Class产物需要符合xxx版本的Java规范
```

最后, 在processor所在的模块下, 新建一个文件, `src/main/resources/META-INF/services/javax.annotation.processing.Processor`
注意文件路径, 路径错了会找不到Processor. 
文件内容是这个模块定义的所有AnnotationProcessor的全限定名, 例如`com.hjhjw1991.barney.serviceprovider.annotation.ServiceProcessor`

2. 调试processor
大致过程跟调试gradle脚本是一样的.
   
首先新建一个Remote JVM debug配置, 端口可以选localhost:5005, 其他不用变. 

然后运行以下命令, 该命令启动一个gradle并等待调试器attach. 在attach之前, 这个命令都不会继续执行下去. 
`./gradlew clean :app:assembleDebug -Dorg.gradle.debug=true --no-daemon`

如果报错```错误: 找不到或无法加载主类 org.gradle.wrapper.GradleWrapperMain```
看一下 gradle/wrapper/gradle-wrapper.jar 是不是不存在, 如果不存在, 去别的工程拷贝一个过来. (这个文件会被git忽略, 本地工程同步的时候会去下载)

如果报错
```A problem occurred evaluating project ':app'.
> Failed to apply plugin 'com.android.internal.application'.
> Android Gradle plugin requires Java 11 to run. You are currently using Java 1.8.
You can try some of the following options:
- changing the IDE settings.
- changing the JAVA_HOME environment variable.
- changing `org.gradle.java.home` in `gradle.properties`.
```
按它的提示把编译环境和运行环境改一致就行. 最佳方式是在工程内部指定统一的Java版本, 例如gradle 7.0.2 版本要求Java在11以上, 
所以我按照最后一条指示, 在 gradle.properties 文件里指定了AS自带的JRE路径

最后, 在processor代码加断点.然后在工具栏切换到刚刚新建的remote debug配置, 点击debug按钮开始调试.  

跟APK调试一样, 当调试线程成功attach后, debug tab 的 console 会显示 attach 信息  
`Connected to the target VM, address: 'localhost:5005', transport: 'socket'`
这时候你应该会看到第2步里终端上执行的那个命令开始继续执行了. 

# 2. 把SPI等可以静态注册的静态注册, 完成解耦和编译期注入