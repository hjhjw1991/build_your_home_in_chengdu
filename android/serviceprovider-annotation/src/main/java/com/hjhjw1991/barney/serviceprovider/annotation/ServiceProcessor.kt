package com.hjhjw1991.barney.serviceprovider.annotation

import com.hjhjw1991.barney.serviceprovider.annotation.ServiceProcessor.Companion.SERVICE_IMPL
import com.hjhjw1991.barney.serviceprovider.annotation.ServiceProcessor.Companion.SERVICE_SPI
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement

@SupportedAnnotationTypes(SERVICE_SPI, SERVICE_IMPL)
class ServiceProcessor: AbstractProcessor() {
    companion object {
        const val SERVICE_SPI = "com.hjhjw1991.barney.serviceprovider.annotation.ServiceInterface"
        const val SERVICE_IMPL = "com.hjhjw1991.barney.serviceprovider.annotation.ServiceImpl"
    }
    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        // 获取实现了SPI接口的实现类, 可能有多个
        // 构造一个代理类, 将获取到的配置注入这个代理类
        // 通过反射使用代理类
        // process annotation and inject services map
        println("HJSPI process")
        return false
    }

    override fun getSupportedOptions(): MutableSet<String> {
        return mutableSetOf("MODULE_NAME")
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

//    override fun getSupportedAnnotationTypes(): MutableSet<String> {
//        return mutableSetOf(
//            SERVICE_SPI,
//            SERVICE_IMPL
//        )
//    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        println("HJSPI init module: " + processingEnv.options["MODULE_NAME"])
    }
}