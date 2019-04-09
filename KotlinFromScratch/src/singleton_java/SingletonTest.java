package singleton_java;

public class SingletonTest {
    private BillPughSingleton bInstance1;
    private EagerInitializingSingleton eInstance1;
    private EnumSingleton enum1;
    private LazyInitializingSingleton lInstance1;
    private SafeLazyInitializingSingleton sInstance1;

    public SingletonTest() {
        bInstance1 = BillPughSingleton.getInstance();
        eInstance1 = EagerInitializingSingleton.getInstance();
        enum1 = EnumSingleton.INSTANCE;
        lInstance1 = LazyInitializingSingleton.getInstance();
        sInstance1 = SafeLazyInitializingSingleton.getInstance();
    }

    public BillPughSingleton getBillPughSingleton() {
        return bInstance1;
    }

    public EagerInitializingSingleton getEagerInitializingSingleton() {
        return eInstance1;
    }

    public EnumSingleton getEnumSingleton() {
        return enum1;
    }

    public LazyInitializingSingleton getLazyInitializingSingleton() {
        return lInstance1;
    }

    public SafeLazyInitializingSingleton getSafeLazyInitializingSingleton() {
        return sInstance1;
    }
}