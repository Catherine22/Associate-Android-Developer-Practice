package singleton_java;

/**
 * 单例模式：保证整个应用中某个实例有且只有一个。
 * <p>
 * 懒汉模式-双重校验锁(与其用懒汉模式不如直接用内部静态类{@link BillPughSingleton})
 * <p>
 * 保证在多线程的情况下，仍只有一个实例。
 *
 */
public class SafeLazyInitializingSingleton {
    private volatile static SafeLazyInitializingSingleton instance = null; //声明成 volatile

    public static SafeLazyInitializingSingleton getInstance() {
        //instance已经实例化后下次进入不必执行synchronized获取对象锁，从而提高性能。
        if (instance == null) {
            synchronized (SafeLazyInitializingSingleton.class) {
                if (instance == null)
                    instance = new SafeLazyInitializingSingleton();
            }
        }
        return instance;
    }

    public void print() {
        System.out.println("Singleton:SafeLazyInitializingSingleton");
    }
}