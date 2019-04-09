package singleton_java;

/**
 * synchronized 示范
 *
 * @author Catherine
 */
public class SynchronizedSample {
    private static int p1;
    private static int p2;
    private static int p3;
    private static int p4;
    private static int p5;

    private static class SynchronizedSampleHolder {
        private static SynchronizedSample syncInstance = new SynchronizedSample();
    }

    public static SynchronizedSample getInstance() {
        return SynchronizedSampleHolder.syncInstance;
    }

    /**
     * 没有synchronized，静态变量的值就会同时被两个线程给修改，因为在{@link #increaseNoSync}
     * 里的逻辑是先读值在重新赋值， 当下读到的值可能已经被另一个线程给修改。
     */
    public void testStaticParamsAsUsual() {

        System.out.println("Run an ordinary method with a static integer in 2 threads.");
        try {
            Thread t1 = new Thread(r1);
            t1.start();
            Thread t2 = new Thread(r1);
            t2.start();

            // Wait for threads to die.
            t1.join();
            t2.join();

            analyze(p1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用synchronized修饰{@link #increase}，线程锁的对象就是{@link #increase}
     * ，由于一个对象只有一把锁，只要被一个线程获取锁，其它线程就不能获取该对象的其他synchronized实例方法。
     */
    public void testStaticParams() {
        System.out.println("Run a synchronized method with a static integer in 2 threads.");
        try {
            Thread t1 = new Thread(r2);
            t1.start();
            Thread t2 = new Thread(r2);
            t2.start();

            // Wait for threads to die.
            t1.join();
            t2.join();

            analyze(p2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同{@link #testStaticParams}，逻辑都一样，但是如果同样的方法({@link #increaseP2}和
     * {@link #increaseP3}完全一样)是来自不同的对象，该方法的锁也会不一样（因为一个对象只有一把锁）。
     */
    public void testStaticParamsFromDifferentObj() {
        System.out.println("Run a synchronized method with a static integer in 2 threads from 2 objects.");
        try {
            SynchronizedSample ss1 = new SynchronizedSample();
            SynchronizedSample ss2 = new SynchronizedSample();

            Thread t1 = new Thread(ss1.r3);
            t1.start();
            Thread t2 = new Thread(ss2.r3);
            t2.start();

            // Wait for threads to die.
            t1.join();
            t2.join();

            analyze(p3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testStaticMethod() {
        testStaticMethod1();
        testStaticMethod2();
    }

    public void testStaticMethodAndSyncCodes() {
        testStaticMethod3();
        testStaticMethod4();
    }

    /**
     * 同{@link #testStaticParams}，逻辑都一样，用静态方法 {@link #staticIncrease}
     * ，即使来自不同的对象，该方法的锁也会一样。 <br>
     * <br>
     * 用此方法保证在整个应用里，同时只会有一个线程执行该方法。
     */
    public void testStaticMethod1() {
        System.out.println("Run a static synchronized method with a static integer in 2 threads from 2 objects.");
        try {
            SynchronizedSample ss1 = new SynchronizedSample();
            SynchronizedSample ss2 = new SynchronizedSample();

            Thread t1 = new Thread(ss1.r4);
            t1.start();
            Thread t2 = new Thread(ss2.r4);
            t2.start();

            // Wait for threads to die.
            t1.join();
            t2.join();

            analyze(p4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 模拟对数据库的两种操作，先呼叫{@link #testStaticMethod1}
     * 进行累计，呼叫此方法进行累乘，预期结果是能先执行完累计再执行累乘，但是同步锁只有针对指定的那一个方法，所以两个方法来自不同锁并不会排程。 <br>
     * <br>
     * 呼叫不同的静态方法同时进行数据的修改，同步锁无法保证一次只有（一个线程）执行一个方法。
     */
    public void testStaticMethod2() {
        System.out.println("Run 2 static synchronized method with a static integer in 2 threads from 2 objects.");
        try {
            SynchronizedSample ss1 = new SynchronizedSample();
            SynchronizedSample ss2 = new SynchronizedSample();

            Thread t1 = new Thread(ss1.r5);
            t1.start();
            Thread t2 = new Thread(ss2.r5);
            t2.start();

            // Wait for threads to die.
            t1.join();
            t2.join();

            analyze(p4);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同{@link #testStaticMethod1}，逻辑都一样，但是在做synchronized时是同步代码块而不是整个方法， 好处有两点：
     * <br>
     * 1. 只需要同步synchronized指定的代码块，提升效能；<br>
     * 2. synchronized的锁自行指定，也就是只要指定同一把锁，就可以实现“呼叫不同的静态方法同时进行数据的修改，保证一次只有（一个线程）
     * 执行一个方法。”，让来自不同对象的方法能在多线程中排队实现。
     */
    public void testStaticMethod3() {
        System.out.println(
                "Run a static method with a static integer which is synchronized a part of codes in 2 threads from 2 objects.");
        try {
            SynchronizedSample ss1 = new SynchronizedSample();
            SynchronizedSample ss2 = new SynchronizedSample();

            Thread t1 = new Thread(ss1.r6);
            t1.start();
            Thread t2 = new Thread(ss2.r6);
            t2.start();

            // Wait for threads to die.
            t1.join();
            t2.join();

            analyze(p5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * 同{@link #testStaticMethod2}，逻辑都一样，但是在做synchronized时是同步代码块而不是整个方法， 好处有两点：
     * <br>
     * 1. 只需要同步synchronized指定的代码块，提升效能；<br>
     * 2. synchronized的锁自行指定，也就是只要指定同一把锁，就可以实现“呼叫不同的静态方法同时进行数据的修改，保证一次只有（一个线程）
     * 执行一个方法。”，让来自不同对象的方法能在多线程中排队实现。
     */
    public void testStaticMethod4() {
        System.out.println(
                "Run 2 static method with a static integer which are synchronized a part of codes in 2 threads from 2 objects.");
        try {
            SynchronizedSample ss1 = new SynchronizedSample();
            SynchronizedSample ss2 = new SynchronizedSample();

            Thread t1 = new Thread(ss1.r7);
            t1.start();
            Thread t2 = new Thread(ss2.r7);
            t2.start();

            // Wait for threads to die.
            t1.join();
            t2.join();

            analyze(p5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void increaseNoSync() {
        for (int i = 0; i < 1000000; i++) {
            p1++;
        }
    }

    private synchronized void increaseP2() {
        for (int i = 0; i < 1000000; i++) {
            p2++;
        }
    }

    private synchronized void increaseP3() {
        for (int i = 0; i < 1000000; i++) {
            p3++;
        }
    }

    private static synchronized void staticIncrease() {
        for (int i = 0; i < 1000000; i++) {
            p4++;
        }
    }

    private static synchronized void staticMultiply() {
        for (int i = 0; i < 10; i++) {
            p4 *= 2;
        }
    }

    private static void staticIncreaseAndSyncCodes() {
        synchronized (SynchronizedSample.getInstance()) {
            for (int i = 0; i < 1000000; i++) {
                p5++;
            }
        }
    }

    private static void staticMultiplyAndSyncCodes() {
        synchronized (SynchronizedSample.getInstance()) {
            for (int i = 0; i < 5; i++) {
                p5 *= 2;
            }
        }
    }

    protected Runnable r1 = this::increaseNoSync;

    protected Runnable r2 = this::increaseP2;

    protected Runnable r3 = this::increaseP3;

    protected Runnable r4 = SynchronizedSample::staticIncrease;

    protected Runnable r5 = SynchronizedSample::staticMultiply;

    protected Runnable r6 = SynchronizedSample::staticIncreaseAndSyncCodes;

    protected Runnable r7 = SynchronizedSample::staticMultiplyAndSyncCodes;

    private void analyze(int num) {
        if (num % 2000000 != 0)
            System.out.println(String.format("--> Result=%d, failed to synchronize.", num));
        else
            System.out.println(String.format("--> Result=%d, synchronized successfully.", num));
    }
}