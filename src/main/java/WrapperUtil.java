import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

public class WrapperUtil {

    public static <T> void doWork(Consumer<T> method, T obj){
        method.accept(obj);
    }
    public static <T,K> K doWork(Function<T,K> method, T obj){
        return method.apply(obj);
    }
    public static <T> void doWork(Consumer<T[]> method, T... obj){
        method.accept(obj);
    }
    public static <T,K> K doWork(Function<T[],K> method, T... obj){
        return method.apply(obj);
    }

    public static void main(String[] args){
        doWork((o) -> {
            System.out.println(Arrays.toString(o));
        },"this","that");
    }
}
