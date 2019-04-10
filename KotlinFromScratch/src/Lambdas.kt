import java.lang.Exception

class Lambdas {
    fun test() {
        println("Higher-order functions")
        handlerError {
            sendLog(null)
        }
        handlerError {
            getDailyReport()
        }

        println("")
        println("Replace callbacks - Java style")
        val javaButton = JavaButton()
        javaButton.setOnClickListener(object : OnClickListener {
            override fun onClick() = println("onClick")
        })
        javaButton.monkeyTest()

        println("")
        println("Replace callbacks - Kotlin style")
        // By using higher-order functions, the above code could be optimised
        val kotlinButton = KotlinButton()
        kotlinButton.setOnClickListener {
            println("onClick")
        }
        kotlinButton.monkeyTest()

        println("")
        println("Lambda functions")
        val add1 = { a: Int, b: Int -> a + b }
        println(add1(3, 7))

        val add2 = this::sum // this refer to a function, e.g. String::format
        println(add2(1, 9))

        println("")
        println("Pass functions as arguments to another function")
        val greetings = listOf("h", "I", ",", " ", "tHeRE!")

        println(doMap(greetings, ::toUpperCase).joinToString("", "Convert to upper case String -> "))
        println(doMap(greetings, ::toLowerCase).joinToString("", "Convert to lower case String -> "))

        println("")
        println("Passing a lambda to the last parameter")
        foo("s", false) { x, y -> "sum($x, $y) = ${x + y}" }


        println("")
        println("Implement a function type as an interface")
        print("[Override invoke] ")
        val tutorialView1 = { message: String -> println("Init tutorial -> $message") }
        tutorialView1.invoke("Page 1")

        print("[Run invoke] ")
        val tutorialView2 = TutorialView()
        tutorialView2.invoke("Page 2")

        println("")
        println("Invoke")
        val join: (String, String) -> String = String::plus
        val append: String.(String) -> String = String::plus
        println(join("Hello, ", "world!"))
        println(join.invoke("Hello, ", "world!"))
        println("Hello, ".append("world!"))

        val joinInt: Int.(Int) -> String = this::joinIntString
        println(joinInt(1, 2))
        println(joinInt.invoke(3, 4))
        println(5.joinInt(6))
    }


    private fun getDailyReport(): String {
        return "{\"id\":\"csvDailyIncidentReport\", \"name\":\"Daily incidents\"}"
    }

    private fun sendLog(logs: String?) {
        if (logs == null)
            throw NullPointerException()
    }

    private fun <T> handlerError(function: () -> T) {
        try {
            val result = function()
            println(result)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun sum(a: Int, b: Int) = a + b

    private fun <T> doMap(list: List<T>, func: (it: T) -> Any) = list.map { func(it) }

    private fun toUpperCase(s: String) = s.map {
        if (it.isLetter()) it.toUpperCase() else it
    }.joinToString("")

    private fun toLowerCase(s: String) = s.map {
        if (it.isLetter()) it.toLowerCase() else it
    }.joinToString("")

    private fun joinIntString(a: Int, b: Int) = "$a$b"

    private fun foo(a: String, b: Boolean, c: (x: Int, y: Int) -> String) {
        println("a: $a")
        println("b: $b")
        println(c(23, 84))
    }

}

interface OnClickListener {
    fun onClick()
}

class JavaButton {

    private var onClickListener: OnClickListener? = null

    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    fun monkeyTest() {
        for (i in 0 until 3) {
            onClickListener?.onClick()
        }
    }
}

class KotlinButton {
    private var onClickListener: (() -> Unit)? = null

    fun setOnClickListener(onClickListener: () -> Unit) {
        this.onClickListener = onClickListener
    }

    fun monkeyTest() {
        for (i in 0 until 3) {
            onClickListener?.invoke()
        }
    }
}

class TutorialView : (String) -> Unit {
    override fun invoke(p1: String) {
        // This code won't be executed
        println("TutorialView: invoke($p1)")
    }
}