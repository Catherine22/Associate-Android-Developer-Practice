class PropertiesAndFields {

    fun test() {
        /*
        Tips:
        var <propertyName>[: <PropertyType>] [= <property_initializer>]
            [<getter>]
            [<setter>]
         */

        println("Getter & setter")
        val quote = Quote()
        quote.value = "I think therefore I am"
        println("${quote.value} Is empty? ${quote.isEmpty}")

        println("")
        println("const")
        foo()

        println("")
        println("lateinit")
        val myTest = MyTest()
        myTest.setup()
        myTest.test()

        println("")
        println("::")
        println(calculate(1, 3, ::plus))

    }

    companion object {
        const val SUBSYSTEM_DEPRECATED: String = "This subsystem is deprecated"
    }

    @Deprecated(SUBSYSTEM_DEPRECATED)
    fun foo() {
        println("foo()")
    }

    fun plus(a: Int, b: Int): Int {
        return a + b
    }

    fun calculate(a: Int, b: Int, method: (Int, Int) -> Int): Int {
        return method(a, b)
    }
}

class Quote {
    var size = 0
        set(value) {
            if (value >= 0) {
                field = value
            }
        }

    val isEmpty: Boolean
        get() = this.size == 0

    var value: String = "\"\""
        set(sentence) {
            size = sentence.length
            field = "\"$sentence\""
        }
}

class MyTest {
    lateinit var subject: TestSubject
    fun setup() {
        subject = TestSubject()
    }

    fun test() {
        subject.method()
    }
}

class TestSubject {
    fun method() {
        println("method")
    }
}