@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")
package com.hjhjw1991.barney.serviceprovider.annotation

import com.hjhjw1991.barney.serviceprovider.annotation.ServiceProcessor.Companion.SERVICE_IMPL
import com.hjhjw1991.barney.serviceprovider.annotation.ServiceProcessor.Companion.SERVICE_SPI
import com.squareup.javapoet.*
import com.sun.source.util.Trees
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.code.Type
import com.sun.tools.javac.code.Types
import com.sun.tools.javac.processing.JavacProcessingEnvironment
import com.sun.tools.javac.tree.TreeMaker
import com.sun.tools.javac.util.Names
import java.io.IOException
import java.util.concurrent.ConcurrentHashMap
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

@SupportedAnnotationTypes(SERVICE_SPI, SERVICE_IMPL)
open class ServiceProcessor: AbstractProcessor() {
    companion object {
        const val SERVICE_SPI = "com.hjhjw1991.barney.serviceprovider.annotation.ServiceInterface"
        const val SERVICE_IMPL = "com.hjhjw1991.barney.serviceprovider.annotation.ServiceImpl"
        const val SERVICE_PROXY = "ServiceManager_Proxy"
        val serviceMapConfig = mutableMapOf<Type, MutableSet<TypeElement>>()
    }
    // apt 相关类
    protected val filer: Filer get() = EnvUtil.filer

    private var packageName: String = ""

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

        // 获取所有ServiceInterface标记的接口
        roundEnv.getElementsAnnotatedWith(ServiceInterface::class.java)
            .filterIsInstance<TypeElement>()
            .forEach { element ->
                println("process find interface = $element")
                serviceMapConfig[(element as Symbol).type] = mutableSetOf()
            }

        // 获取所有ServiceImpl标记的类
        roundEnv.getElementsAnnotatedWith(ServiceImpl::class.java)
            .filterIsInstance<TypeElement>()
            .forEach { element ->
                println("process find class = $element")
                element.interfaces.filterIsInstance<Type>()
                    .find { it in serviceMapConfig }
                    ?.let {
                    serviceMapConfig[it]?.add(element)
                }
            }

        // todo 检查注解参数, 接口继承关系
        try {
            serviceMapConfig.keys.forEach { type ->
                createJavaFileByJavaPoet(type, serviceMapConfig[type])
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        println(serviceMapConfig)
        println("process end !!!")
        return true
    }

    // create generated class in app/build/generated/source/kapt/
    private fun createJavaFileByJavaPoet(curInterface: Type, curElements: MutableSet<TypeElement>?) {
        if (curElements.isNullOrEmpty()) {
            return
        }
        val curElement = curElements.first()
        /*
            public static final ConcurrentHashMap mServices = new ConcurrentHashMap<Class, Object>()
        */
        val serviceField = FieldSpec.builder(ConcurrentHashMap::class.java, "mServices")
            .addModifiers(Modifier.STATIC, Modifier.PUBLIC, Modifier.FINAL)
            .initializer(CodeBlock.of("new ConcurrentHashMap<Class, Object>()"))
            .build()
        val init = MethodSpec.methodBuilder("init")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(TypeName.VOID)
            .addStatement("\$T.out.println(\$S)", System::class.java, curElement.simpleName)
            .build()

        val initialize = mutableListOf<CodeBlock>()
        initialize.add(CodeBlock.of(
            "mServices.put(\$L.class, new \$L());", curInterface.tsym.simpleName, curElement.simpleName)
        )
        val proxy = TypeSpec.classBuilder(SERVICE_PROXY)
            .addField(serviceField)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addStaticBlock(CodeBlock.join(
                initialize,""
            ))
            .addMethod(init)
            .build()
        val javaFile = JavaFile.builder(packageName, proxy).build()
        try {
            javaFile.writeTo(filer)
        } catch (th: Throwable) {
            th.printStackTrace()
        }
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
        println("HJSPI init module: " + processingEnv.options["PACKAGE_NAME"])
        packageName = processingEnv.options["PACKAGE_NAME"].orEmpty()
        EnvUtil.init(processingEnv)
    }
}

internal object EnvUtil {
    private var env: ProcessingEnvironment? = null

    // apt 相关类
    lateinit var filer: Filer              //文件相关的辅助类
    lateinit var elements: Elements    //元素相关的辅助类
    lateinit var messager: Messager        //日志相关的辅助类

    // javac 编译器相关类
    lateinit var trees: Trees
    lateinit var treeMaker: TreeMaker
    lateinit var names: Names
    lateinit var types: Types

    fun init(env: ProcessingEnvironment) {
        if (EnvUtil.env != null) {
            return
        }
        EnvUtil.env = env

        filer = env.filer
        elements = env.elementUtils
        messager = env.messager

        trees = Trees.instance(env)
        val context = (env as JavacProcessingEnvironment).context
        treeMaker = TreeMaker.instance(context)
        names = Names.instance(context)
        types = Types.instance(context)
    }
}