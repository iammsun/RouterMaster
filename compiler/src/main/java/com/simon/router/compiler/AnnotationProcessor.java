package com.simon.router.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.MethodSpec;
import com.simon.router.annotations.Call;
import com.simon.router.annotations.Nav;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;

/**
 * Created by sunmeng on 2017/2/16.
 */
@AutoService(Processor.class)
public class AnnotationProcessor extends AbstractProcessor {

    private static final String OPTION_MODULE = "router_module";
    private static final String OPTION_ROOT = "router_is_root";
    private static final String OPTION_DEPENDENCIES = "router_dependencies";

    static final String PKG = "com.simon.router";

    private String moduleName;
    private boolean root = true;
    private String[] dependencies;
    private boolean genDone;
    private MapGenerator callGenerator;
    private MapGenerator navGenerator;
    private Generator routerGenerator;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        handleOptions();
        callGenerator = new CallGenerator(processingEnv, moduleName);
        navGenerator = new NavGenerator(processingEnv, moduleName);
        routerGenerator = new Generator(processingEnv, PKG, "RouterInit");
    }

    @Override
    public Set<String> getSupportedOptions() {
        Set<String> ret = new HashSet<>();
        ret.add(OPTION_MODULE);
        ret.add(OPTION_ROOT);
        ret.add(OPTION_DEPENDENCIES);
        return ret;
    }

    private void handleOptions() {
        moduleName = processingEnv.getOptions().get(OPTION_MODULE);
        if (moduleName != null) {
            root = Boolean.parseBoolean(processingEnv.getOptions().get(OPTION_ROOT));
        }
        String dependenciesStr = processingEnv.getOptions().get(OPTION_DEPENDENCIES);
        if (dependenciesStr != null && dependenciesStr.length() > 0) {
            dependencies = dependenciesStr.split(",");
        }
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> ret = new HashSet<>();
        ret.add(Nav.class.getCanonicalName());
        ret.add(Call.class.getCanonicalName());
        // process always
        ret.add(Override.class.getCanonicalName());
        return ret;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (genDone) {
            return false;
        }
        genDone = true;
        if (root) {
            genRouterInit();
        }
        return navGenerator.genMap(roundEnvironment.getElementsAnnotatedWith(Nav.class))
                && callGenerator.genMap(roundEnvironment.getElementsAnnotatedWith(Call.class));
    }

    private void genRouterInit() {
        MethodSpec.Builder initMethod = MethodSpec.methodBuilder("init")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC);
        if (dependencies == null || dependencies.length == 0) {
            initMethod.addStatement(CallGenerator.CLASS_NAME + ".map()");
            initMethod.addStatement(NavGenerator.CLASS_NAME + ".map()");
        } else {
            for (String module : dependencies) {
                initMethod.addStatement(CallGenerator.CLASS_NAME + module + ".map()");
                initMethod.addStatement(NavGenerator.CLASS_NAME + module + ".map()");
            }
        }
        routerGenerator.genClass(initMethod);
    }
}
