package singleton_java;

/**
 * 单例模式：保证整个应用中某个实例有且只有一个。
 * <p>
 * 饿汉模式
 * <p>
 * 多线程沒有問題，因为JVM只会加载一次单例类。
 * 特点是加载类时比较慢，但运行时获取对象的速度比较快，线程安全
 * 单例对象初始化非常快，而且占用内存非常小的时候这种方式是比较合适的，可以直接在应用启动时加载并初始化。
 */
public class EagerInitializingSingleton {
    private static EagerInitializingSingleton instance = new EagerInitializingSingleton();

    private EagerInitializingSingleton() {
    }

    public static EagerInitializingSingleton getInstance() {
        return instance;
    }

    public void print() {
        System.out.println("Singleton:EagerInitializingSingleton");
    }
}