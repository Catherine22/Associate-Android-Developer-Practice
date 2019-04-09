package singleton_kotlin

class SingletonTest {
    val billPughSingleton: BillPughSingleton = BillPughSingleton.getInstance()
    val eagerInitializingSingleton: EagerInitializingSingleton = EagerInitializingSingleton
    val enumSingleton: EnumSingleton = EnumSingleton.INSTANCE
    val lazyInitializingSingleton: LazyInitializingSingleton = LazyInitializingSingleton.instance
    val safeLazyInitializingSingleton: SafeLazyInitializingSingleton = SafeLazyInitializingSingleton.instance
}