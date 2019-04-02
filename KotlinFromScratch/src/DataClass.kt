class DataClass {
    fun test() {
        println("Properties declared in the primary constructor")
        val scout = User("Scout", 15, "F")
        val (name, age) = scout
        println("$name, $age years of age")

        val jack = User(age = 12, name = "Jack")
        val olderJack = jack.copy(age = 20)
        println("$jack.name, $jack.age years of age")
        println("$olderJack.name, $olderJack.age years of age")


        println("")
        println("Properties declared in the class body")
        val employee = Employee("Michael")
        employee.age = 18
    }
}