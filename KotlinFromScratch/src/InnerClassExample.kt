class InnerClassExample {
    val id = 1

    class NestedClass {
        fun foo() = 2 // cannot access id in InnerClassExample
    }

    inner class InnerClass {
        fun foo() = id
    }

    fun test() {
        println("Nested class")
        println(NestedClass().foo())


        println("")
        println("Inner class")
        println(InnerClass().foo())

        println("")
        println("Anonymous Inner class")
    }
}