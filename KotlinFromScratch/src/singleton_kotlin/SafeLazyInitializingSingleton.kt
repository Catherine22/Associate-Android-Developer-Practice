package singleton_kotlin

/**
 * 单例模式：保证整个应用中某个实例有且只有一个。
 * <p>
 * 懒汉模式-双重校验锁(与其用懒汉模式不如直接用内部静态类{@link BillPughSingleton})
 * <p>
 * 保证在多线程的情况下，仍只有一个实例。
 *
 */
class SafeLazyInitializingSingleton {
    companion object {
        val instance by lazy(LazyThreadSafetyMode.SYNCHRONIZED) { SafeLazyInitializingSingleton() }
        fun print() = println("Singleton:SafeLazyInitializingSingleton")
    }
}