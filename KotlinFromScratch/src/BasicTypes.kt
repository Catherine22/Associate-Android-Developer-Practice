import java.lang.Double.NaN

// https://kotlinlang.org/docs/reference/basic-types.html
class BasicTypes {
    // literal constants: make numbers more readable
    private val oneMillion = 1_000_000
    private val creditCardNumbers = 1234_5678_9012_3456


    fun test() {
        println("Literal constants: make numbers more readable")
        println(oneMillion)
        println(creditCardNumbers)

        println("")
        println("Structural equality(==) vs referential equality(===)")

        val a: Int = 10000
        val boxedA: Int? = a
        val anotherBoxedA: Int? = a
        println(boxedA === a)
        println(boxedA === anotherBoxedA)

        println("")
        println("Bitwise operations")

        val number272 = 0x00110
        println("0x00110 & 0x00001 = " + (number272 and 0x00001 == 0x00000))
        println("0x00110 & 0x00010 = " + (number272 and 0x00010 == 0x00010))
        println("0x00110 | 0x00001 = " + (number272 or 0x00001 == 0x00111))
        println("0x00110 | 0x00010 = " + (number272 or 0x00010 == 0x00110))
        println("0x00110 ^ 0x00001 = " + (number272 xor 0x00001 == 0x00111))
        println("0x00110 ^ 0x00010 = " + (number272 xor 0x00010 == 0x00100))


        println("")
        println("Numbers comparison")
        println("NaN == NaN ? " + (NaN == NaN))
        println("-0.0 < 0.0 ? " + (-0.0 < 0.0))
        println("NaN is greater than any other numbers ? " + (Int.MAX_VALUE < NaN))

        println("")
        println("Characters")
        println("a\tb\bc\nd\re\'f\'\"g\"h\\i\$j\uFF00k")


        val poem = "B7ut 1soo6n we2 mu0st ris3e"
        fun filterNumbers(s: String): String {
            var result = ""
            val range = '0'..'9'
            s.forEach {
                if (it !in range) {
                    result += it
                }
            }
            return result
        }
        println(filterNumbers(poem))

        println("")
        println("Strings")

        val phoenixAndTurtle = """
            >Truth may seem but cannot be;
            >Beauty brag but ’tis not she;
            >Truth and beauty buried be.
        """
        println(phoenixAndTurtle)
        println(phoenixAndTurtle.trimIndent())
        println(phoenixAndTurtle.trimMargin(">")) // by default |

        val bucks = 10
        println("I bet \$ $bucks bucks")

        val name = "John"
        println("My name is $name, ${name.map { "\"${it.toUpperCase()}\"" }.joinToString()}")
        println("My name is $name, ${name.map { "${it.toUpperCase()}" }.joinToString("-", "\"", "\"")}")


        println("")
        println("Array")
        val square = Array(10, { i -> i * i })
        print("square array = [")
        square.forEachIndexed { index, i ->
            print(i)
            if (index != square.lastIndex)
                print(", ")
        }
        println("]")

        val numbers: IntArray = intArrayOf(1, 0, 4, 27, 39, -58)
        print("number array = [")
        numbers.forEachIndexed { index, i ->
            print(i)
            if (index != numbers.lastIndex)
                print(", ")
        }
        println("]")
    }
}