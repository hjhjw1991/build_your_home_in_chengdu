@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")
package com.hjhjw1991.barney.serviceprovider.annotation

import com.hjhjw1991.barney.serviceprovider.annotation.ServiceProcessor.Companion.SERVICE_IMPL
import com.hjhjw1991.barney.serviceprovider.annotation.ServiceProcessor.Companion.SERVICE_SPI
import com.squareup.javapoet.*
import com.sun.source.tree.Tree
import com.sun.source.util.Trees
import com.sun.tools.javac.code.Flags
import com.sun.tools.javac.code.Symbol
import com.sun.tools.javac.code.Type
import com.sun.tools.javac.code.Types
import com.sun.tools.javac.processing.JavacProcessingEnvironment
import com.sun.tools.javac.tree.JCTree
import com.sun.tools.javac.tree.TreeMaker
import com.sun.tools.javac.tree.TreeTranslator
import com.sun.tools.javac.util.List as JList
import com.sun.tools.javac.util.ListBuffer
import com.sun.tools.javac.util.Name
import com.sun.tools.javac.util.Names
import java.io.IOException
import javax.annotation.processing.*
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.util.Elements

@SupportedAnnotationTypes(SERVICE_SPI, SERVICE_IMPL)
class ServiceProcessor: AbstractProcessor() {
    companion object {
        const val SERVICE_SPI = "com.hjhjw1991.barney.serviceprovider.annotation.ServiceInterface"
        const val SERVICE_IMPL = "com.hjhjw1991.barney.serviceprovider.annotation.ServiceImpl"
        const val SERVICE_PROXY = "ServiceManager_Proxy"
        val serviceMapConfig = mutableMapOf<Type, MutableSet<String>>()
    }
    // apt 相关类
    protected val filer: Filer get() = EnvUtil.filer
    protected val elements: Elements get() = EnvUtil.elements
    protected val messager: Messager get() = EnvUtil.messager

    // javac 编译器相关类
    protected val trees: Trees get() = EnvUtil.trees
    protected val treeMaker: TreeMaker get() = EnvUtil.treeMaker
    protected val names: Names get() = EnvUtil.names

    private var mFields: MutableList<JCTree.JCVariableDecl> = JList.nil()

    protected var rootTree: JCTree.JCCompilationUnit? = null

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
            .filterIsInstance<TypeElement>()
            .forEach { element ->
                println("process find interface = $element")
                serviceMapConfig[(element as Symbol).type] = mutableSetOf()
            }
        roundEnv.getElementsAnnotatedWith(ServiceImpl::class.java)
            .filterIsInstance<TypeElement>()
            .forEach { element ->
                val treePath = trees.getPath(element)
                val cu = treePath.compilationUnit as JCTree.JCCompilationUnit
                rootTree = cu
                println("process find class = $element, jcTree = ${cu.javaClass.simpleName}")
                element.interfaces.filterIsInstance<Type>()
                    .find { it in serviceMapConfig }
                    ?.let {
                    serviceMapConfig[it]?.add(element.qualifiedName.toString())
                }
                translate(element, trees.getTree(element) as JCTree)

                try {
                    generateJavaFile(element, trees.getTree(element) as JCTree)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

        println(serviceMapConfig)
        println("process end !!!")
        return true
    }

    fun translate(curElement: TypeElement, curTree: JCTree) {
        println("translate")
        curTree.accept(MyTreeTranslator(curElement.simpleName as Name))
    }

    @Throws(IOException::class)
    fun generateJavaFile(curElement: TypeElement, curTree: JCTree) {
        println("generateJavaFile")
        getJavaFile(curElement).writeTo(filer)
        getJavaFileByJavaPoet(curElement, curTree)
    }

    fun getJavaFile(curElement: TypeElement): JavaFile {
        val packageName = rootTree?.packageName?.toString() ?: ""

        val curIGetter = TypeSpec.interfaceBuilder("${curElement.qualifiedName.substring(packageName.length + 1).replace(".", "$$")}\$\$IGetter")
            .addModifiers(Modifier.PUBLIC)
            .apply {
                mFields.forEach {
                    this.addMethod(
                        MethodSpec.methodBuilder(it.name.toString())
                        .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                        .returns(ClassName.get(it.sym.asType()))
                        .build()
                    )
                }
            }
            .build()

        return JavaFile.builder(packageName, curIGetter)
            .build()
    }

    // get generated class in app/build/intermediates/javac/package_name/
    fun getJavaFileByJavaPoet(curElement: TypeElement, curTree: JCTree) {
        val packageName = rootTree?.packageName?.toString() ?: ""
        val init = MethodSpec.methodBuilder("init")
            .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
            .returns(TypeName.VOID)
            .addStatement("\$T.out.println(\$S)", System::class.java, curElement.simpleName)
            .build()
        val proxy = TypeSpec.classBuilder(SERVICE_PROXY)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
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
        EnvUtil.init(processingEnv)
    }

    inner class MyTreeTranslator(private val rootClazzName: Name) : TreeTranslator() {
        override fun visitClassDef(jcClassDecl: JCTree.JCClassDecl) {
            if (jcClassDecl.name == rootClazzName) {
                mFields = JList.nil()
                jcClassDecl.defs
                    .filter { it.kind == Tree.Kind.VARIABLE }
                    .map { it as JCTree.JCVariableDecl }
                    .forEach {
                        mFields.add(it)
                        jcClassDecl.defs.add(makeGetterMethodDecl(it))
                    }
            }
            super.visitClassDef(jcClassDecl)
        }

        /**
         *   public String getName() {
         *      return this.name;
         *   }
         */
        private fun makeGetterMethodDecl(jcVariableDecl: JCTree.JCVariableDecl): JCTree.JCMethodDecl {
            val body = ListBuffer<JCTree.JCStatement>()
                .append(treeMaker.Return(treeMaker.Select(treeMaker.Ident(names._this), jcVariableDecl.getName())))
                .toList()
                .let { treeMaker.Block(0, it) }

            return treeMaker.MethodDef(
                treeMaker.Modifiers(Flags.PUBLIC.toLong()),
                names.fromString(jcVariableDecl.getName().toString()),
                jcVariableDecl.vartype,
                JList.nil(), JList.nil(), JList.nil(), body, null)
        }
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