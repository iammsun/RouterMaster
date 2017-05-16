package com.simon.router.compiler;

import com.simon.router.annotations.Nav;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * Created by sunmeng on 2017/3/9.
 */

public class NavGenerator extends MapGenerator {

    static final String CLASS_NAME = "NavRouter";

    private Map<String, Element> actions = new HashMap();

    public NavGenerator(ProcessingEnvironment processingEnv, String moduleName) {
        super(processingEnv, moduleName == null ? CLASS_NAME : (CLASS_NAME + moduleName));
    }

    @Override
    protected boolean genMapItem(MethodSpec.Builder mapMethod, Element element) {
        Nav nav = element.getAnnotation(Nav.class);
        if (nav == null) {
            messager.printMessage(Diagnostic.Kind.ERROR, "@Nav lost:\n" + element);
            return false;
        }
        ClassName className = ClassName.get((TypeElement) element);
        for (String action : nav.value()) {
            if (action == null || action.length() == 0) {
                action = String.format("%s.%s", className.packageName(), className.simpleName());
            }
            if (actions.containsKey(action)) {
                messager.printMessage(Diagnostic.Kind.ERROR, String.format("@Nav %s " +
                        "duplicate:\nexist " + "%s\nnew %s", action, actions.get(action), element));
                return false;
            }
            mapMethod.addStatement(String.format(METHOD_STATEMENT, "$T.class"), action, className);
            actions.put(action, element);
        }
        return true;
    }
}
