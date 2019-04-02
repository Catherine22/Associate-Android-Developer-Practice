package package1

import BaseClass

class BaseClassExtensions {
    private fun BaseClass.bar() {
        println("bar() from package1.BaseClassExtensions")
        println(this@BaseClassExtensions.toString())
        println(toString())
    }

    fun caller(e: BaseClass) {
        e.bar()
    }

    override fun toString(): String {
        return "toString() from BaseClassExtensions"
    }
}