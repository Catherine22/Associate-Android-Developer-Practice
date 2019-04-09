typealias Pass = (Int) -> Boolean

class TypeAliases {

    private fun testResultMessage(isPass: Boolean): String = if (isPass) "Pass!" else "Fail"

    fun test() {
        println("Type aliases")
        val mathTest: (Int) -> Boolean = { it > 100 }
        val geographyTest: Pass = { it > 50 }
        val mark = 70
        println("Got $mark on math test -> ${testResultMessage(mathTest(mark))}")
        println("Got $mark on geography test -> ${testResultMessage(geographyTest(mark))}")
    }
}