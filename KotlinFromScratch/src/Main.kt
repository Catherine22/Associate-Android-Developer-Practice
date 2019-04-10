import singleton_java.*

fun main(args: Array<String>) {
//    val basicTypes = BasicTypes()
//    basicTypes.test()

//    val controlFlow = ControlFlow()
//    controlFlow.test()

//    val returnsAndJumps = ReturnsAndJumps()
//    returnsAndJumps.test()

//    val myClass = MyClass()
//    myClass.test()

//    val propertiesAndFields = PropertiesAndFields()
//    propertiesAndFields.test()

//    val baseClass = BaseClass()
//    baseClass.test()

//    val dataClass = DataClass()
//    dataClass.test()

//    val sealedClass = SealedClass()
//    sealedClass.test()

//    val generics = Generics()
//    generics.test()

//    val innerClassExample = InnerClassExample()
//    innerClassExample.test()

//    val enumClasses = EnumClasses()
//    enumClasses.test()

//    val objects = Objects()
//    objects.test()

//    val typeAliases = TypeAliases()
//    typeAliases.test()

//    val inlineClass = InlineClass()
//    inlineClass.test()

//    val delegation = Delegation()
//    delegation.test()

//    val delegateProperties = DelegatedProperties()
//    delegateProperties.test()

//    testJavaSingleton()
//    testKotlinSingleton()

//    val functions = Functions()
//    functions.test()

    val lambdas = Lambdas()
    lambdas.test()

}

fun testJavaSingleton() {
    val bInstance1 = BillPughSingleton.getInstance()
    val eInstance1 = EagerInitializingSingleton.getInstance()
    val enum1 = EnumSingleton.INSTANCE
    val lInstance1 = LazyInitializingSingleton.getInstance()
    val sInstance1 = SafeLazyInitializingSingleton.getInstance()

    Thread {
        val bInstance2 = BillPughSingleton.getInstance()
        val eInstance2 = EagerInitializingSingleton.getInstance()
        val enum2 = EnumSingleton.INSTANCE
        val lInstance2 = LazyInitializingSingleton.getInstance()
        val sInstance2 = SafeLazyInitializingSingleton.getInstance()

        val sTest = SingletonTest()
        if (bInstance1 === bInstance2 && bInstance1 === sTest.billPughSingleton)
            println("Singleton\tBillPughSingleton 同一个实例")
        else
            println("Singleton\tBillPughSingleton 不同实例")

        if (eInstance1 === eInstance2 && eInstance1 === sTest.eagerInitializingSingleton)
            println("Singleton\tEagerInitializingSingleton 同一个实例")
        else
            println("Singleton\tEagerInitializingSingleton 不同实例")

        if (enum1 == enum2 && enum1 == sTest.enumSingleton)
            println("Singleton\tEnumSingleton 同一个实例")
        else
            println("Singleton\tEnumSingleton 不同实例")

        if (lInstance1 === lInstance2 && lInstance1 === sTest.lazyInitializingSingleton)
            println("Singleton\tLazyInitializingSingleton 同一个实例")
        else
            println("Singleton\tLazyInitializingSingleton 不同实例")

        if (sInstance1 === sInstance2 && sInstance1 === sTest.safeLazyInitializingSingleton)
            println("Singleton\tSafeLazyInitializingSingleton 同一个实例")
        else
            println("Singleton\tSafeLazyInitializingSingleton 不同实例")
    }.start()
}


fun testKotlinSingleton() {
    val bInstance1 = singleton_kotlin.BillPughSingleton.getInstance()
    val eInstance1 = singleton_kotlin.EagerInitializingSingleton
    val enum1 = singleton_kotlin.EnumSingleton.INSTANCE
    val lInstance1 = singleton_kotlin.LazyInitializingSingleton.instance
    val sInstance1 = singleton_kotlin.SafeLazyInitializingSingleton.instance

    Thread {
        val bInstance2 = singleton_kotlin.BillPughSingleton.getInstance()
        val eInstance2 = singleton_kotlin.EagerInitializingSingleton
        val enum2 = singleton_kotlin.EnumSingleton.INSTANCE
        val lInstance2 = singleton_kotlin.LazyInitializingSingleton.instance
        val sInstance2 = singleton_kotlin.SafeLazyInitializingSingleton.instance

        val sTest = singleton_kotlin.SingletonTest()
        if (bInstance1 === bInstance2 && bInstance1 === sTest.billPughSingleton)
            println("Singleton\tBillPughSingleton 同一个实例")
        else
            println("Singleton\tBillPughSingleton 不同实例")

        if (eInstance1 === eInstance2 && eInstance1 === sTest.eagerInitializingSingleton)
            println("Singleton\tEagerInitializingSingleton 同一个实例")
        else
            println("Singleton\tEagerInitializingSingleton 不同实例")

        if (enum1 == enum2 && enum1 == sTest.enumSingleton)
            println("Singleton\tEnumSingleton 同一个实例")
        else
            println("Singleton\tEnumSingleton 不同实例")

        if (lInstance1 === lInstance2 && lInstance1 === sTest.lazyInitializingSingleton)
            println("Singleton\tLazyInitializingSingleton 同一个实例")
        else
            println("Singleton\tLazyInitializingSingleton 不同实例")

        if (sInstance1 === sInstance2 && sInstance1 === sTest.safeLazyInitializingSingleton)
            println("Singleton\tSafeLazyInitializingSingleton 同一个实例")
        else
            println("Singleton\tSafeLazyInitializingSingleton 不同实例")
    }.start()
}