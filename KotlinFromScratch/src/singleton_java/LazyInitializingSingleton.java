package singleton_java;

/**
 * 单例模式：保证整个应用中某个实例有且只有一个。
 * <p>
 * 懒汉模式(与其用懒汉模式不如直接用内部静态类{@link BillPughSingleton})
 * <p>
 * 在多线程的情况下，可能就会出现重复创建单例对象的问题。 特点是加载类时比较快，但运行时获取对象的速度比较慢，线程不安全
 * 某个单例用的次数不是很多，但是这个单例提供的功能又非常复杂，而且加载和初始化要消耗大量的资源，这个时候使用懒汉式就是非常不错的选择。
 */
public class LazyInitializingSingleton {
    private static LazyInitializingSingleton instance = null;

    private LazyInitializingSingleton() {
    }

    public static LazyInitializingSingleton getInstance() {
        if (instance == null)
            instance = new LazyInitializingSingleton();
        return instance;
    }

    public void print() {
        System.out.println("Singleton:LazyInitializingSingleton");
    }
}
