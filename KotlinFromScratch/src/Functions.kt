class Functions {
    fun test() {
        println("Basic functions")
        println(double(10))
        println(triple(10))
        println(isPrimeNumber(17))

        println("")
        println("Default arguments")
        val s = "Function parameters can have default values, which are"
        read(s.toByteArray())

        println("")
        println("Override functions")
        val b = B()
        b.foo()

        println("")
        println("lambda")
        foo(0, 2) { println("Fill in all arguments") }
        foo(0) { println("Fill in some arguments") }
        foo(baz = 2) { println("Fill in specific arguments") }
        foo(qux = { println("Fill in the last argument") })
        foo { println("Fill in the last argument") }


        println("")
        println("variable number of arguments (vararg)")
        foo(strings = *arrayOf("a", "b", "c"))

        println("")
        println("Unit-returning functions")
        println("fun bar(): Unit {} is equivalent to fun bar() {}")


        println("")
        println("infix functions")
        println("aaa".add(3))
        println("aaa" add 3)
        this bar "bbb"
        bar("bbb")

        println("")
        println("Local functions")
        f()
    }

    private fun double(x: Int): Int = 2 * x
    private fun triple(x: Int) = 3 * x
    private fun isPrimeNumber(x: Int): Boolean {
        var factors = 0
        for (i in 2..x / 2) {
            if (x % i == 0) {
                factors++
            }
        }
        return factors == 0
    }

    private fun read(bytes: ByteArray, off: Int = 0, len: Int = bytes.size) {
        val s = String(bytes, off, len)
        println(s)
    }

    open class A {
        open fun foo() {}
    }

    class B : A() {
        override fun foo() {}
    }

    private fun foo(bar: Int = 0, baz: Int = 1, qux: () -> Unit) {
        qux()
    }

    // In Java -> public void println(String.. args) { }
    private fun foo(vararg strings: String) {
        println(strings[0])
    }

    private infix fun String.add(x: Int) = "$this$x"
    private infix fun bar(s: String) = println(s)

    private fun f() {
        fun f() {
            println("call f() in f()")
        }
        print("call f() -> ")
        f()
    }
}