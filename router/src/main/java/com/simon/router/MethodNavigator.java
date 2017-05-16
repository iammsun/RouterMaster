package com.simon.router;

import android.net.Uri;

/**
 * Created by sunmeng on 16/8/15.
 */
class MethodNavigator extends MappingNavigator<MethodHolder> {

    @Override
    void map(String format, MethodHolder target) {
        Uri uri = Uri.parse(format);
        MethodHolder old = navByParamTypes(uri, target.getParamTypes());
        if (old != null) {
            throw new IllegalStateException(
                    String.format("duplicate:\n%s->%s\n%s->%s", format, old, format, target));
        }
        MAPPING_LIST.add(new Mapping<>(uri, target));
    }

    @Override
    MethodHolder nav(Uri uri) {
        return nav(uri, new Object[]{});
    }

    MethodHolder nav(Uri uri, Object... objects) {
        Class[] paramTypes = null;
        if (objects != null) {
            paramTypes = new Class[objects.length];
            for (int index = 0; index < objects.length; index++) {
                paramTypes[index] = objects[index] == null ? null : objects[index].getClass();
            }
        }
        return navByParamTypes(uri, paramTypes);
    }

    /**
     * 是否可以自动转型
     *
     * @param clazz
     * @param primitiveClass
     * @return
     */
    private static boolean isAcceptByPrimitive(Class clazz, Class primitiveClass) {
        if ((clazz == Boolean.class || clazz == boolean.class)
                && primitiveClass == boolean.class) {
            return true;
        } else if ((clazz == Character.class || clazz == char.class)
                && (primitiveClass == char.class || primitiveClass == int.class
                || primitiveClass == long.class || primitiveClass == float.class
                || primitiveClass == double.class)) {
            return true;
        } else if ((clazz == Byte.class || clazz == byte.class)
                && (primitiveClass == byte.class || primitiveClass == short.class
                || primitiveClass == int.class || primitiveClass == long.class
                || primitiveClass == float.class || primitiveClass == double.class)) {
            return true;
        } else if ((clazz == Short.class || clazz == short.class)
                && (primitiveClass == short.class || primitiveClass == int.class
                || primitiveClass == long.class || primitiveClass == float.class
                || primitiveClass == double.class)) {
            return true;
        } else if ((clazz == Integer.class || clazz == int.class)
                && (primitiveClass == int.class || primitiveClass == long.class
                || primitiveClass == float.class || primitiveClass == double.class)) {
            return true;
        } else if ((clazz == Long.class || clazz == long.class)
                && (primitiveClass == long.class || primitiveClass == float.class
                || primitiveClass == double.class)) {
            return true;
        } else if ((clazz == Float.class || clazz == float.class)
                && (primitiveClass == float.class || primitiveClass == double.class)) {
            return true;
        } else if ((clazz == Double.class || clazz == double.class)
                && primitiveClass == double.class) {
            return true;
        } else if ((clazz == Void.class || clazz == void.class) && primitiveClass == void.class) {
            return true;
        }
        return false;
    }

    MethodHolder navByParamTypes(Uri uri, Class[] paramTypes) {
        for (Mapping<MethodHolder> mapping : MAPPING_LIST) {
            if (match(mapping, uri, paramTypes)) {
                return mapping.getTarget();
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static boolean match(Mapping<MethodHolder> mapping, Uri uri, Class[] paramTypes) {
        if (!mapping.match(uri)) {
            return false;
        }
        MethodHolder holder = mapping.getTarget();
        int declaredParamSize = holder.getParamTypes() == null ? 0 : holder.getParamTypes().length;
        int paramSize = paramTypes == null ? 0 : paramTypes.length;
        if (declaredParamSize != paramSize) {
            return false;
        }
        if (declaredParamSize == 0) {
            return true;
        }
        for (int index = 0; index < holder.getParamTypes().length; index++) {
            Class declaredType = holder.getParamTypes()[index];
            Class paramType = paramTypes[index];
            if (paramType == null && declaredType.isPrimitive()) {
                return false;
            }
            if (paramType == null) {
                continue;
            }
            if (declaredType.isPrimitive() && isAcceptByPrimitive(paramType, declaredType)) {
                continue;
            }
            if (declaredType.isAssignableFrom(paramType) || paramType.isAssignableFrom(declaredType)) {
                continue;
            }
            return false;
        }
        return true;
    }
}
