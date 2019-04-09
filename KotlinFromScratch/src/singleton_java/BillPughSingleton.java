package singleton_java;

/**
 * 单例模式：保证整个应用中某个实例有且只有一个。
 * <p>
 * 内部静态类
 * 特点是加载类时比较快，但运行时获取对象的速度比较慢，线程安全
 * 只要应用中不使用内部类 JVM 就不会去加载这个单例类，也就不会创建单例对象，从而实现懒汉式的延迟加载和线程安全。
 * 加载BillPughSingleton时，并没有加载SingletonHolder，这就是为啥能延迟加载。
 * 为什么是线程安全是因为虚拟机有对类加载做处理，确保类加载一定是线程安全。
 */
public class BillPughSingleton {
    private BillPughSingleton() {
    }

    //内部类，在装载该内部类时才会去创建单利对象
    private static class SingletonHolder {
        private static BillPughSingleton instance = new BillPughSingleton();
    }

    public static BillPughSingleton getInstance() {
        return SingletonHolder.instance;
    }

    public void print() {
        System.out.println("Singleton:BillPughSingleton");
    }
}
