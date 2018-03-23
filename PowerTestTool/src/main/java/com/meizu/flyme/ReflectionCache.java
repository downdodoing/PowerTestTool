package com.meizu.flyme;

import android.util.LruCache;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;

public class ReflectionCache {
    public static final String TAG = "ReflectionCache";
    private static final int DEFAULT_REFLECTINON_CACHE_SIZE = 1024 * 1; // 1MB
    private LruCache<String, ClassInfo> mClassInfoCache;
    public static HashMap<String, ClassInfo> classInfoMap;

    private ReflectionCache() {
        classInfoMap = new HashMap<>();
        mClassInfoCache = new LruCache<String, ClassInfo>(DEFAULT_REFLECTINON_CACHE_SIZE) {

            @Override
            protected int sizeOf(String key, ClassInfo value) {
                return 1;
            }
        };
    }

    private static class SingletonHolder {
        private static final ReflectionCache INSTANCE = new ReflectionCache();
    }

    //单例模式创建 
    public static final ReflectionCache build() {
        return SingletonHolder.INSTANCE;
    }
    
    private void putClassInfoToCache(String key, ClassInfo classInfo) {
        classInfoMap.put(key, classInfo);
    }

    private ClassInfo getClassInfoFromCache(String key) {
        return classInfoMap.get(key);
    }
    
    public Class<?> forName(String className) throws ClassNotFoundException {
        return forName(className, true);
    }
    public Class<?> forName(String className, Boolean isCached) throws ClassNotFoundException {
        if (isCached) {
            ClassInfo classInfoFromCache = getClassInfoFromCache(className);
            if (classInfoFromCache != null) {
                return classInfoFromCache.mClass;
            } else {
                Class c = Class.forName(className);
                ClassInfo classInfo = new ClassInfo(c, className);
                putClassInfoToCache(className, classInfo);
                return c;
            }
        } else {
            return Class.forName(className);
        }
    }

    public Method getMethod(Class<?> objClass, String methodName, boolean isDeclared,Class<?>... parameterTypes) throws
            NoSuchMethodException {
        ClassInfo classInfoFromCache = getClassInfoFromCache(objClass.getName());
        String methodKey = methodName;
        Method method = null;
        for (Class<?> c : parameterTypes) {
            methodKey = methodKey + c.toString();
        }
        if (classInfoFromCache != null) {
            Method methodFromCache = classInfoFromCache.getCachedMethod(methodKey);
            if (methodFromCache != null) {
                method =  methodFromCache;
            } else {
                if (!isDeclared) {
                    method = objClass.getMethod(methodName, parameterTypes);
                } else {
                    method = objClass.getDeclaredMethod(methodName, parameterTypes);
                    method.setAccessible(true);
                }
                classInfoFromCache.addCachedMethod(methodKey, method);
            }
        } else {
            if (!isDeclared) {
                method = objClass.getMethod(methodName, parameterTypes);
            } else {
                method = objClass.getDeclaredMethod(methodName, parameterTypes);
                method.setAccessible(true);
            }
        }
        return method;
    }

    public Field getField(Class<?> objClass, String fieldName, boolean isDeclared) throws NoSuchFieldException {
        ClassInfo classInfoFromCache = getClassInfoFromCache(objClass.getName());
        Field field = null;
        if (classInfoFromCache != null) {
            Field fieldFromCache = classInfoFromCache.getCachedField(fieldName);
            if (fieldFromCache != null) {
                field = fieldFromCache;
            } else {
                if (!isDeclared) {
                    field = objClass.getField(fieldName);
                } else {
                    field = objClass.getDeclaredField(fieldName);
                    field.setAccessible(true);
                }
                classInfoFromCache.addCachedField(fieldName, field);
            }
        } else {
            field = objClass.getField(fieldName);
        }
        return field;
    }


    /**
     *
     * @param objClass
     * @param methodName
     * @param isDeclared
     * @param parameterTypes
     * @return
     */
    public static Method getCachedMethod(Class<?> objClass, String methodName, boolean isDeclared, Class<?>... parameterTypes ) {

        Method b = null;
        try {
            ReflectionCache.build().forName(objClass.getName(),true);
            b = ReflectionCache.build().getMethod(objClass, methodName,isDeclared, parameterTypes);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return b;
    }

    public static Field getCachedField(Class<?> objClass,String fieldName,boolean isDeclared) {
        Field field = null;
        try {
            ReflectionCache.build().forName(objClass.getName());
            field = ReflectionCache.build().getField(objClass,fieldName,isDeclared);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return  field;

    }


}
