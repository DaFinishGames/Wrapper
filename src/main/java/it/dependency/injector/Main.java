package it.dependency.injector;

import it.dependency.injector.annotations.Injected;
import it.dependency.injector.annotations.InjectionEnabled;
import it.dependency.injector.core.DependencyInjector;
import it.dependency.injector.testing.ITestingDependency;

@InjectionEnabled
public class Main {

    @Injected
    public static ITestingDependency dependency;

    public static void main(String[] args){
        DependencyInjector dip = new DependencyInjector();
        dip.initializeInjector(Main.class);
        dependency.doThing();
    }
}