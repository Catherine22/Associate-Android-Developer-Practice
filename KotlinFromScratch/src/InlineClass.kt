// since Kotlin 1.3, it is impossible to call functions which accept inline classes from Java.
class InlineClass {

    fun test() {
        println("Inline Classes")
        val martin = Name("Martin")
        martin.greet()
        println(martin.length)

        println("")
        println("Representation")
        asInline(martin)
        asGeneric(martin)
        asInterface(martin)
        asNullable(martin)
        println(id(martin))

        println("")
        println("Inline classes vs type aliases")
        val nameAlias: NameTypeAlias = ""
        val nameInlineClass: NameInlineClass = NameInlineClass("")
        val s: String = ""

        acceptString(nameAlias)
//        acceptString(nameInlineClass) // not allow

        acceptNameTypeAlias(s)
//        acceptNameInlineClass(s) // not allow

    }

    private fun asInline(n: Name) {}
    private fun <T> asGeneric(t: T) {}
    private fun asInterface(i: I) {}
    private fun asNullable(n: Name?) {}
    private fun <T> id(t: T): T = t

    private fun acceptString(s: String) {}
    private fun acceptNameTypeAlias(n: NameTypeAlias) {}
    private fun acceptNameInlineClass(n: NameInlineClass) {}
}

interface I

inline class Name(private val s: String) : I {
    val length: Int
        get() = s.length

    fun greet() {
        println("Hello, $s")
    }
}

inline class NameInlineClass(val s: String)
typealias NameTypeAlias = String