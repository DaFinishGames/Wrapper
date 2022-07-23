package it.dependency.injector.core;

import it.dependency.injector.annotations.Injected;
import it.dependency.injector.util.ClassUtil;
import it.dependency.injector.util.FileUtil;
import it.dependency.injector.util.LoggerUtil;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class DependencyInjector {

    private static final LoggerUtil _log = new LoggerUtil(DependencyInjector.class.getName());

    private Map<String,Object> injectionEnabled = new HashMap<>();
    private Map<String,Object> injected = new HashMap<>();

    public void initializeInjector(Class<?> activator){
        _log.setLogLevel(Level.OFF);
        String packageName = activator.getPackageName();
        File path = new File("src\\main\\java\\" +packageName.replace(".","\\"));
        if (path.isDirectory()) {
            List<File> allFiles = FileUtil.getAllFiles(path.getPath());
            for(File f : allFiles){
                _log.info("------------------ File: " + f.getName() + " ------------------");
                Class<?> fileClass = ClassUtil.getClassOfFilePath(activator.getClassLoader(),f.getAbsolutePath(),packageName);
                Annotation[] annotations = fileClass.getDeclaredAnnotations();
                _log.info("annotations: " + Arrays.toString(annotations));
                boolean hasInjectionEnabled = Arrays.stream(annotations).anyMatch(
                        a -> a.annotationType().getName().contains(DependencyInjectorEnum.InjectionEnabled.name())
                );
                _log.info("hasInjectionEnabled: " + hasInjectionEnabled);
                if(hasInjectionEnabled){
                    if(injectionEnabled.get(fileClass.getName()) == null){
                        _log.info("injectionEnabled map has no fileClass: " + fileClass.getName() + " value. Getting from injected.");
                        Object alreadyInjected = injected.getOrDefault(fileClass.getName(), null);
                        injectionEnabled.put(fileClass.getName(),alreadyInjected);
                    }
                    Field[] fields = ClassUtil.extractFieldsWithAnnotation(
                            fileClass,DependencyInjectorEnum.Injected.name());
                    _log.info("fields: " + Arrays.toString(fields));
                    for (Field field : fields){
                        Annotation[] fieldAnnotations = field.getDeclaredAnnotations();
                        _log.info("fieldAnnotations: " + Arrays.toString(fieldAnnotations));
                        boolean hasInjected = Arrays.stream(fieldAnnotations).anyMatch(
                                a -> a.annotationType().getName().contains(DependencyInjectorEnum.Injected.name())
                        );
                        _log.info("hasInjected: "+hasInjected);
                        if(hasInjected){
                            if(injected.get(field.getType().getName()) == null){
                                _log.info("injected map ha no value for field: " +field.getType().getName() + " getting from injectionEnabled");
                                Object alreadyInjected = injectionEnabled.getOrDefault(
                                        field.getType().getName(),null);
                                injected.put(field.getType().getName(),alreadyInjected);
                            }
                            _log.info("beginning injection");
                            injectIntoClassObject(fileClass,field);
                            _log.info("finished injection");
                        }
                    }
                }
            }
        }
        _log.info("Initialization finished");
    }


    private void injectIntoClassObject(Class<?> clazz,Field field){
        if(injectionEnabled.containsKey(clazz.getName())){
            Object instanceOfFileClass = ClassUtil.generateClassInstance(clazz,injectionEnabled);
            Injected fieldAnnotation = field.getAnnotation(Injected.class);
            Object instanceOfField = ClassUtil.generateClassInstance(
                    fieldAnnotation.className() == DefaultClassInjected.class ?
                            field.getType() : fieldAnnotation.className(),injected);
            try {
                field.set(instanceOfFileClass,instanceOfField);
            } catch (IllegalAccessException e) {
                _log.error("The field: " + field.getName() +
                        " has wrong visibility on class: " + clazz.getName());
                throw new RuntimeException(e);
            }
        }
    }


}
