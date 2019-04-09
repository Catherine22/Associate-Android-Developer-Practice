class Delegation {

    fun test() {
        println("Implementation by delegation")
        val baseImpl = BaseImpl(10)
        Derived(baseImpl).println() // message from BaseImpl, if you want to show message from Derived, override println()
        Derived(baseImpl).print()
        println(Derived(baseImpl).message) // message from Derived
    }

    interface Base {
        val message: String
        fun print()
        fun println()
    }

    class BaseImpl(x: Int) : Base {
        override val message = "BaseImpl: x = $x"

        override fun print() {
            print(message)
        }

        override fun println() {
            println(message)
        }
    }

    // The last "b" (from ": Base by b") is implemented by the "b" in "Derived(b: Base)"
    class Derived(b: Base) : Base by b {

        override val message = "Message of Derived"
        // overriding functions is optional
        override fun print() {
            print("Derived\n")
        }

        // override println() or println the message from BaseImpl
//        override fun println() {
//            println(message)
//        }
    }
}