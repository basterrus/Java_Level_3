package test;

import interfaces.AfterSuite;
import interfaces.BeforeSuite;
import interfaces.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;

public class TestingClass {

    public static void start(Class<?> classForTest){
        testMethodsClassForTest(classForTest);
    }

    private static void testMethodsClassForTest(Class<?> classForTest) {

        Object objectClassForTest = null;
        try {
            objectClassForTest = classForTest.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        LinkedList<Method> methodList = getMethodList(classForTest);
        for (Method method : methodList) {
            method.setAccessible(true);
            try {
                method.invoke(objectClassForTest);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private static LinkedList<Method> getMethodList(Class<?> ClassForTest) {

        LinkedList<Method> classMethods = new LinkedList<>();
        boolean beforeSuite = false;
        boolean afterSuite = false;

        for (Method method : ClassForTest.getDeclaredMethods()){
            if (method.isAnnotationPresent(Test.class)){
                classMethods.add(method);
            }
            classMethods.sort((method1, method2) -> {
                return method2.getAnnotation(Test.class).priority() - method1.getAnnotation(Test.class).priority();
            });
        }

        for (Method method : ClassForTest.getDeclaredMethods()){
            if (method.isAnnotationPresent(BeforeSuite.class)){
                if (!beforeSuite){
                    classMethods.addFirst(method);
                    beforeSuite = true;
                } else {
                    throw new RuntimeException("Any methods with annotate @interfaces.BeforeSuite");
                }
            }
            if (method.isAnnotationPresent(AfterSuite.class)){
                if (!afterSuite){
                    classMethods.addLast(method);
                    afterSuite = true;
                } else {
                    throw new RuntimeException("Any methods with annotate @interfaces.AfterSuite");
                }
            }
        }

        for(Method method : classMethods) System.out.println(method);
        return classMethods;
    }
}
