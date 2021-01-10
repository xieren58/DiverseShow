package com.four.app_init_processor

import com.four.app_init_handler.api.OnAppLifeChanged
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement

data class AppLifeElement(val executableElement: ExecutableElement,
                          val typeElement: TypeElement,
                          val packageElement: PackageElement,
                          val taskData: OnAppLifeChanged
)
