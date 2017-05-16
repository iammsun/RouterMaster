package com.simon.router.compiler;

import com.squareup.javapoet.MethodSpec;

import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;

/**
 * Created by sunmeng on 2017/3/9.
 */

public abstract class MapGenerator extends Generator {

    protected static final String METHOD_STATEMENT = AnnotationProcessor.PKG
            + ".Navigator.map($S, %s)";

    public MapGenerator(ProcessingEnvironment processingEnv, String className) {
        super(processingEnv, AnnotationProcessor.PKG, className);
    }

    /**
     * 构建路由map
     *
     * @param elements
     * @return
     */
    public final boolean genMap(Set<? extends Element> elements) {
        MethodSpec.Builder mapMethod = MethodSpec.methodBuilder("map")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC);

        if (elements != null && !elements.isEmpty()) {
            for (Element element : elements) {
                if (!genMapItem(mapMethod, element)) {
                    return false;
                }
            }
        }
        return genClass(mapMethod);
    }

    /**
     * 构建路由map item
     *
     * @param mapMethod
     * @param element
     * @return
     */
    protected abstract boolean genMapItem(MethodSpec.Builder mapMethod, Element element);
}
