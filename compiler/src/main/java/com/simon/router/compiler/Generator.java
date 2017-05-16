package com.simon.router.compiler;

import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.tools.Diagnostic;

/**
 * Created by sunmeng on 2017/3/9.
 */

public class Generator {

    protected final Messager messager;
    protected final Filer filer;
    private final String packageName;
    private final String className;

    public Generator(ProcessingEnvironment processingEnv, String packageName, String className) {
        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
        this.packageName = packageName;
        this.className = className;
    }

    /**
     * 生成class
     *
     * @param mapMethod
     * @return
     */
    protected final boolean genClass(MethodSpec.Builder mapMethod) {
        TypeSpec routerMapping = TypeSpec.classBuilder(className)
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addMethod(mapMethod.build())
                .build();
        try {
            JavaFile.builder(packageName, routerMapping).build().writeTo(filer);
        } catch (Throwable e) {
            messager.printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            return false;
        }
        return true;
    }
}
