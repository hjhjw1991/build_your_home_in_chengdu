package com.hjhjw1991.barney.serviceprovider.annotation

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.RoundEnvironment
import javax.annotation.processing.SupportedAnnotationTypes
import javax.lang.model.element.TypeElement

//@AutoService
@SupportedAnnotationTypes("com.hjhjw1991.barney.serviceprovider.annotation.ServiceImpl")
class ServiceProcessor: AbstractProcessor() {
    override fun process(
        annotations: MutableSet<out TypeElement>?,
        roundEnv: RoundEnvironment?
    ): Boolean {
        // process annotation and inject service map
        println(roundEnv)
        return false
    }
}