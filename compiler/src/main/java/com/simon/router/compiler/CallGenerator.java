package com.simon.router.compiler;

import com.simon.router.annotations.Call;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.MethodSpec;
import com.sun.tools.javac.code.Type;
import com.sun.tools.javac.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic;

/**
 * Created by sunmeng on 2017/3/9.
 */

public class CallGenerator extends MapGenerator {

    static final String CLASS_NAME = "CallRouter";

    private static String getHolderStatement(int paramSize) {
        String statement = "new %s.MethodHolder($S, new $T[]{";
        for (int index = 0; index < paramSize; index++) {
            statement += "$T.class";
            if (index != paramSize - 1) {
                statement += ",";
            }
        }
        statement += "}, $T.class)";
        return String.format(statement, AnnotationProcessor.PKG);
    }

    CallGenerator(ProcessingEnvironment processingEnv, String moduleName) {
        super(processingEnv, moduleName == null ? CLASS_NAME : (CLASS_NAME + moduleName));
    }

    @Override
    protected boolean genMapItem(MethodSpec.Builder mapMethod, Element element) {
        Call call = element.getAnnotation(Call.class);
        if (call == null) {
            messager.printMessage(Diagnostic.Kind.ERROR, "@Call lost:\n" + element);
            return false;
        }
        Name methodName = element.getSimpleName();
        for (String action : call.value()) {
            if (action == null || action.length() == 0) {
                action = String.valueOf(methodName);
            }
            ClassName className = ClassName.get((TypeElement) element.getEnclosingElement());
            Type.MethodType methodType = (Type.MethodType) element.asType();
            List<Type> paramTypes = methodType.getParameterTypes();
            String statement = getHolderStatement(paramTypes.size());
            Object[] params = new Object[4 + paramTypes.size()];
            params[0] = action;
            params[1] = methodName;
            params[2] = ClassName.get(Class.class);
            for (int index = 3; index < 3 + paramTypes.size(); index++) {
                params[index] = paramTypes.get(index - 3);
            }
            params[3 + paramTypes.size()] = className;
            mapMethod.addStatement(String.format(METHOD_STATEMENT, statement), params);
        }
        return true;
    }

}
