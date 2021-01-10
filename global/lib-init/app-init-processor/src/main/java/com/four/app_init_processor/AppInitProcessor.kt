package com.four.app_init_processor

import com.four.app_init_handler.api.OnAppLifeChanged
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.*
import javax.lang.model.util.Elements
import javax.lang.model.util.Types

class AppInitProcessor : AbstractProcessor() {

    private lateinit var typeUtil: Types
    private lateinit var filer: Filer
    private lateinit var elementUtil: Elements

    override fun init(p0: ProcessingEnvironment) {
        super.init(p0)
        typeUtil = p0.typeUtils
        filer = p0.filer
        elementUtil = p0.elementUtils
        Logger.realLogger = p0.messager
        Logger.d("app init processor init.")
    }

    override fun getSupportedSourceVersion(): SourceVersion {
        return SourceVersion.RELEASE_8
    }

    override fun getSupportedAnnotationTypes(): MutableSet<String> {
        return mutableSetOf(OnAppLifeChanged::class.qualifiedName.toString())
    }

    /**
     *
     * @see PackageElement
     * @see ExecutableElement
     * @see TypeElement
     * @see VariableElement
     */
    override fun process(dataSet: MutableSet<out TypeElement>?, environment: RoundEnvironment?): Boolean {
        if (dataSet == null || environment == null) return false

        val targets = environment.getElementsAnnotatedWith(OnAppLifeChanged::class.java)
        if (targets.isNullOrEmpty()) return false
        targets.forEach {
            if (it.kind != ElementKind.METHOD) {
                Logger.e("OnAppLifeChange must be on method.")
            }
            it as ExecutableElement
            if (!it.modifiers.contains(Modifier.PUBLIC)) {
                Logger.e("${it.simpleName} has no modifiers (public).")
            }
            if(it.parameters.size != 0 && it.parameters.size != 1) {
                Logger.e("${it.simpleName} params can't more than two.")
            }
            val packageElement = elementUtil.getPackageOf(it.enclosingElement)
            val typeElement = it.enclosingElement as TypeElement
            val lifeChanged = it.getAnnotation(OnAppLifeChanged::class.java)
            val element = AppLifeElement(it, typeElement, packageElement, lifeChanged)

            try {
                TaskClassGenerator.generate(element, filer)
            } catch (e: Exception) {
                Logger.e("${e.message}")
            }
        }

        return true
    }
}