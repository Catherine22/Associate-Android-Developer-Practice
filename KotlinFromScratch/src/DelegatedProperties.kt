import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class DelegatedProperties {
    fun test() {
        println("Standard delegates - Lazy")
        val lazyValue: String by lazy {
            println("computed")
            "Hello"
        }
        println("Call lazy value at the first time: $lazyValue")
        println("Call lazy value again: $lazyValue")

        // skip Lazy, read more in singleton_kotlin/
        println("")
        println("Observable")
        val user = User()
        user.name = "first"
        user.name = "second"


        println("")
        println("Storing Properties in a Map")
        val classmate = Classmate(
            mapOf(
                "name" to "John Doe",
                "age" to 25
            )
        )
        println("name: ${classmate.name}, age: ${classmate.age}")
        println("")
        println("Storing Properties in a mutable map")
        val mutableClasses = MutableClassmate(
            mutableMapOf(
                "name" to "John Doe",
                "age" to 25
            )
        )
        mutableClasses.age = 29
        println("name: ${mutableClasses.name}, age: ${mutableClasses.age}")


        println("")
        println("Implement properties including standard delegates once for all")
        val e = Example()
        println(e.p)
        e.p = "new"


        println("")
        println("Local delegated properties")
        f {Foo()}

    }

    private fun f(computeFoo: () -> Foo) {
        val memorizedFoo by lazy(computeFoo)
        if (memorizedFoo.isValid()) {
            memorizedFoo.doSomething()
        }
    }

    class Example {
        var p: String by Delegate()
    }

    class Delegate {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
            return "$thisRef, thank you for delegating '${property.name}' to me!"
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            println("'$value' has been assigned to '${property.name}' in $thisRef.")
        }
    }

    class User {
        var name: String by Delegates.observable("<no name>") { _, oldValue, newValue ->
            println("$oldValue -> $newValue")
        }
    }

    class Classmate(map: Map<String, Any?>) {
        val name: String by map
        val age: Int by map
    }

    class MutableClassmate(map: MutableMap<String, Any?>) {
        var name: String by map
        var age: Int by map
    }

    class Foo {
        fun isValid(): Boolean = true
        fun doSomething() = println("Do something")
    }
}