package com.hjhjw1991.barney.serviceprovider.annotation

import com.hjhjw1991.barney.serviceprovider.annotation.ServiceProcessor.Companion.SERVICE_IMPL
import com.hjhjw1991.barney.serviceprovider.annotation.ServiceProcessor.Companion.SERVICE_SPI
import java.io.IOException
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.lang.model.SourceVersion
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.TypeElement

@SupportedAnnotationTypes(SERVICE_SPI, SERVICE_IMPL)
class ServiceProcessor: AbstractProcessor() {
    companion object {
        const val SERVICE_SPI = "com.hjhjw1991.barney.serviceprovider.annotation.ServiceInterface"
        const val SERVICE_IMPL = "com.hjhjw1991.barney.serviceprovider.annotation.ServiceImpl"
        const val SERVICE_PROXY = "ServiceManager_Proxy"
    }

    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment
    ): Boolean {
        // 获取实现了SPI接口的实现类, 可能有多个
        // 构造一个代理类, 将获取到的配置注入这个代理类
        // 通过反射使用代理类
        // process annotation and inject services map
        println("HJSPI process")
        println("process begin !!! set = $annotations")

        roundEnv.getElementsAnnotatedWith(ServiceInterface::class.java)
            .filter { it is TypeElement }
            .map { it as TypeElement }
            .forEach {
//                val treePath = trees.getPath(it)
//                val cu = treePath.compilationUnit as JCTree.JCCompilationUnit
//                rootTree = cu
//                println("process find class = $it, jcTree = ${cu.javaClass.simpleName}")
                println("process find class = $it")
                translate(it)

                try {
                    generateJavaFile(it)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        println("process end !!!")
        return true
    }

    fun translate(curElement: TypeElement) {
        println("translate")
        var useDCL = false
        var executableElement: ExecutableElement? = null
        curElement.enclosedElements
            .filter { it is ExecutableElement }
            .map { it as ExecutableElement }
            .forEach {
                if (it.getAnnotation(ServiceInterface::class.java) != null && !it.parameters.isEmpty()) {
                    useDCL = true
                    executableElement = it
                    return@forEach
                }
            }
    }

    @Throws(IOException::class)
    fun generateJavaFile(curElement: TypeElement) {
        println("generateJavaFile")
    }

    override fun getSupportedOptions(): MutableSet<String> {
        return mutableSetOf("MODULE_NAME")
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.latestSupported()
    }

    override fun init(processingEnv: ProcessingEnvironment) {
        super.init(processingEnv)
        println("HJSPI init module: " + processingEnv.options["MODULE_NAME"])
    }
}