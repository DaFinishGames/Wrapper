package it.dependency.injector.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

public class ClassUtil {

    private static final LoggerUtil _log = new LoggerUtil(ClassUtil.class.getName());

    public static Object generateClassInstance(Class<?> clazz, Map<String, Object> generatedMap){
        if(clazz.isInterface()){
            try {
                clazz = clazz.getClassLoader().loadClass(clazz.getName()+"Impl");
            } catch (ClassNotFoundException e) {
                _log.error("No Impl class implementing interface: " + clazz.getName());
                throw new RuntimeException(e);
            }
        }
        Object response = generatedMap.get(clazz.getName());
        if(response == null){
            try {
                response = clazz.getDeclaredConstructor().newInstance();
                generatedMap.put(clazz.getName(),response);
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                     NoSuchMethodException e) {
                _log.error("There was a problem on creating a new instance of class: " + clazz.getName());
                throw new RuntimeException(e);
            }
        }
        return response;
    }

    public static Field[] extractFieldsWithAnnotation(Class<?> clazz,String annotationFilter){
        Field[] fields = clazz.getFields();
        return Arrays.stream(fields).filter(f->
                Arrays.stream(f.getDeclaredAnnotations()).anyMatch(
                        a -> a.annotationType().getName().contains(annotationFilter)
                )
        ).toArray(Field[]::new);
    }

    public static Class<?> getClassOfFilePath(ClassLoader loader, String classFilePath, String rootPackageName){
        String fileClassName = classFilePath.substring(classFilePath.indexOf(rootPackageName.replace(".","\\")));
        String className = fileClassName.substring(0,fileClassName.lastIndexOf(".")).replace("\\",".");
        try {
            return loader.loadClass(className);
        } catch (ClassNotFoundException e) {
            _log.error("There was an error when loading the class: " + className);
            throw new RuntimeException(e);
        }
    }
}
