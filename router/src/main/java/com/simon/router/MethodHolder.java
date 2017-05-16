package com.simon.router;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/**
 * Created by sunmeng on 2017/4/27.
 */

class MethodHolder {

    private final String methodName;
    private final Class[] paramTypes;
    private final Class handler;

    MethodHolder(String methodName, Class[] paramTypes, Class handler) {
        this.methodName = methodName;
        this.paramTypes = paramTypes;
        this.handler = handler;
    }

    @SuppressWarnings("unchecked")
    <T> T call(Object... objects) {
        try {
            Method method = handler.getDeclaredMethod(methodName, paramTypes);
            method.setAccessible(true);
            return (T) method.invoke(null, objects);
        } catch (NoSuchMethodException e) {
            throw new MappingNotFoundException(e);
        } catch (IllegalAccessException e) {
            throw new MappingNotFoundException(e);
        } catch (IllegalArgumentException e) {
            throw new MappingNotFoundException(e);
        } catch (InvocationTargetException e) {
            throw new MappingNotFoundException(e);
        }
    }

    String getMethodName() {
        return methodName;
    }

    Class[] getParamTypes() {
        return paramTypes;
    }

    Class getHandler() {
        return handler;
    }

    @SuppressWarnings("unchecked")
    boolean isStatic() {
        try {
            Method method = handler.getDeclaredMethod(methodName, paramTypes);
            return Modifier.isStatic(method.getModifiers());
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String toString() {
        String format = "%s.%s(";
        StringBuffer stringBuffer = null;
        for (Class clazz : paramTypes) {
            if (stringBuffer == null) {
                stringBuffer = new StringBuffer();
                stringBuffer.append(clazz.getSimpleName());
            } else {
                stringBuffer.append(", ").append(clazz.getSimpleName());
            }
        }
        if (stringBuffer == null) {
            format += ")";
        } else {
            format += stringBuffer.toString() + ")";
        }
        return String.format(format, handler.getName(), methodName);
    }
}
