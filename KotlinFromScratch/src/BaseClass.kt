import package1.BaseClassExtensions

class BaseClass {
    fun test() {
        println("BaseClass functions")
        val l = mutableListOf(1, 2, 3)
        l.swap(1, 2)
        println(l)
        hello()
        foo() // member always win

        println("")
        println("Call extension functions of this class declared other class")
        val plugin = BaseClassExtensions()
        plugin.caller(this)

        println("")
        println("Extension properties")
        println(name)

        println("")
        println("Common extensions")

        val a: String? = null
        println(a.toString())

        val list = List(5) { i -> i * i }
        println(list)


        println("")
        println("Companion Objects")
        val e = E.newInstance() // val e = E() is not allowed here
        println(E.INNER_CONSTANT)
        println(F.INNER_CONSTANT)


        println("")
        println("Complicated scenarios")
        val paper = Paper()
        val sketchbook = Sketchbook()
        paper.caller(Node())
        paper.caller(BlackNode())
        sketchbook.caller(Node())
        sketchbook.caller(BlackNode())
    }

    private fun foo() {
        println("member foo()")
    }

    override fun toString(): String {
        return "toString() from BaseClass"
    }

}

// Nullable receiver
fun Any?.toString(): String {
    if (this == null) return "null"
    return toString()
}


// extension functions
fun MutableList<Int>.swap(index1: Int, index2: Int) {
    val tmp = this[index1]
    this[index1] = this[index2]
    this[index2] = tmp
}

fun BaseClass.hello() {
    println("Hello")
}

fun BaseClass.foo() {
    println("Extension foo()")
}

// extension properties
val BaseClass.name: String
    get() = "Extension name"


class E private constructor() {
    companion object {
        const val INNER_CONSTANT = "E"
        fun newInstance() = E()
    }

}

class F {
    companion object CompanionName {
        const val INNER_CONSTANT = "F"
    }
}


open class Node

class BlackNode : Node()

open class Paper {
    open fun Node.draw() {
        println("Draw a node on Paper")
    }

    open fun BlackNode.draw() {
        println("Draw a black node on Paper")
    }

    fun caller(node: Node) {
        node.draw()
    }

    // without this, BlackNode.draw() will never be called
    fun caller(node: BlackNode) {
        node.draw()
    }
}

class Sketchbook : Paper() {
    override fun Node.draw() {
        println("Draw a node in Sketchbook")
    }

    override fun BlackNode.draw() {
        println("Draw a black node in Sketchbook")
    }
}