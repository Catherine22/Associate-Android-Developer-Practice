class MyClass {
    fun test() {
        println("Class with multiple constructors")
        val frank = Person("Frank", "S", "Watson")
        println(frank.fullName())

        println("")
        println("Inheritance")
        val wood = Student("Kent", "Wood")
        println(wood.fullName())

        var bar1 = Bar1(0)
        var bar2 = Bar2()

        println("")
        println("Inheritance (Child -> Parent -> Grandparent)")
        println("0. Initialise Child")
        val child = Child("memo1")
        println("1. Override function in Child")
        child.f()
        println("2. Inherited function from Parent")
        child.g()
        println("3. Call both super and override function in Child")
        child.h()
        println("4. Call both super and override function in inner Class of Parent")
        val parent = Parent("memo2")
        val colleague = parent.Colleague()
        colleague.g()

        println("")
        println("Resolving overriding conflicts")
        val c = C()
        c.foo()
        c.bar()
        println("----")
        val d = D()
        d.foo()
        d.bar()
    }
}

open class Person {

    var firstName: String = ""
    var middleName: String = ""
    var lastName: String = ""

    constructor(firstName: String, lastName: String) {
        this.firstName = firstName
        this.lastName = lastName
    }

    constructor(firstName: String, middleName: String, lastName: String) : this(firstName, lastName) {
        this.middleName = middleName
    }

    var fullName: () -> String = {
        "$firstName $middleName $lastName"
    }
}

class Student : Person {

    constructor(firstName: String, lastName: String) : super(firstName, lastName) {
        this.firstName = firstName
        this.lastName = lastName
    }

    constructor(firstName: String, middleName: String, lastName: String) : this(firstName, lastName) {
        this.middleName = middleName
    }
}


interface Foo {
    val count: Int
}

class Bar1(override val count: Int) : Foo
class Bar2 : Foo {
    override val count = 0
}

open class Grandparent(memo: String) {
    init {
        println("Initialising Grandparent")
    }

    open val id: Int = (0..10000).random().also { println("Initialising id in Grandparent: $it") }
    open fun f() {
        println("Grandparent.f()")
    }
}

open class Parent(memo: String) : Grandparent(memo) {
    init {
        println("Initialising Parent")
    }

    override val id: Int = (0..10000).random().also { println("Initialising id in Parent: $it") }
    override fun f() {
        println("Parent.f()")
    }

    fun g() {
        print("From Grandparent: ")
        super.f()

        print("From Child: ")
        f()
    }

    inner class Colleague {
        fun g() {
            print("From Grandparent: ")
            super@Parent.f()

            print("From Parent: ")
            f()
        }
    }
}


class Child(memo: String) : Parent(memo) {
    init {
        println("Initialising Child")
    }

    override val id: Int = (0..10000).random().also { println("Initialising id in Child: $it") }
    override fun f() {
        println("Child.f()")
    }


    fun h() {
        print("From Parent: ")
        super.f()

        print("From Child: ")
        f()
    }
}

abstract class Base {
    abstract fun f()
}

class Derived : Base() {
    override fun f() {

    }
}


interface A {
    fun foo() {
        println("A")
    }

    fun bar()
}

interface B {
    fun foo() {
        println("B")
    }

    fun bar() {
        println("bar")
    }
}

class C : A {
    override fun bar() {
        println("bar")
    }
}

class D : A, B {
    override fun foo() {
        super<A>.foo()
        super<B>.foo()
    }

    override fun bar() {
        super.bar() // from B
    }
}