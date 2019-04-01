class ControlFlow {

    fun test() {
        println("If Expression")
        val ten = 10
        val twenty = 20
        val max = getMax(ten, twenty)


        println("")
        println("When Expression")


        print("(when 1)fibonacci array = [")
        for (i in 1..10) {
            print(fibonacci1(i))
            if (i != 10)
                print(", ")
        }
        println("]")


        print("(when 2)fibonacci array = [")
        for (i in 1..10) {
            print(fibonacci2(i))
            if (i != 10)
                print(", ")
        }
        println("]")

        print("(when 3)")
        val taxi = Sedan()
        recogniseCar(taxi)


        println("")
        println("For Expression")


        val primes = intArrayOf(2, 3, 5, 7, 11, 13, 17, 23, 29, 31)
        print("(If 1)primes array = [")
        for (num in primes) {
            print("$num ,")
        }
        println("]")


        print("(If 2)primes array = [")
        for (index in 0 until primes.size) {
            print("${primes[index]} ,")
        }
        println("]")


        print("(If 3)primes array = [")
        for (index in primes.indices) {
            print("${primes[index]} ,")
        }
        println("]")


        print("(If 4)primes array = [")
        primes.forEach {
            print("$it ,")
        }
        println("]")


        print("(If 5)primes array = [")
        primes.forEachIndexed { index, i ->
            print(i)
            if (index != primes.lastIndex)
                print(", ")
        }
        println("]")


        print("Iterate over 2 numbers from the bottom. Primes array = [")
        for (index in primes.size - 1 downTo 0 step 2) {
            print("${primes[index]} ,")
        }
        println("]")


        println("")
        println("While Expression")

        print("Factors of 36 = [")
        var factors = findFactors1(36)
        factors.forEachIndexed { index, i ->
            print(i)
            if (index != factors.lastIndex)
                print(", ")
        }
        println("]")


        print("Factors of 42 = [")
        factors = findFactors2(42)
        factors.forEachIndexed { index, i ->
            print(i)
            if (index != factors.lastIndex)
                print(", ")
        }
        println("]")

    }

    private fun getMax(num1: Int, num2: Int): Int {
        return if (num1 > num2) {
            println("$num1 is larger")
            num1
        } else {
            println("$num2 is larger")
            num2
        }
    }

    private fun fibonacci1(size: Int): Int {
        if (size < 1) {
            throw ArrayIndexOutOfBoundsException("size must be positive")
        }
        return when (size) {
            1, 2 -> 1
            else -> {
                fibonacci1(size - 1) + fibonacci1(size - 2)
            }
        }
    }

    private fun fibonacci2(size: Int): Int {
        return when {
            size < 0 -> throw ArrayIndexOutOfBoundsException("size must be positive")
            size == 1 -> 1
            size == 2 -> 1
            else -> (fibonacci2(size - 1) + fibonacci2(size - 2))
        }
    }

    private fun recogniseCar(car: Car) {
        when (car) {
            is Sedan -> println("This is a ${car.getName()}")
            is Suv -> println("This is a ${car.getName()}")
            else -> println("No idea")
        }
    }

    private fun findFactors1(number: Int): IntArray {
        var factors = IntArray(0)
        var i = 1
        while (i <= (number / 2)) {
            if (number.rem(i) == 0) {
                factors += i
            }
            i++
        }
        factors += number
        return factors.sortedArray()
    }

    private fun findFactors2(number: Int): IntArray {
        var factors = IntArray(0)
        var i = 1
        do {
            if (number.rem(i) == 0) {
                factors += i
            }
            i++
        } while (i <= (number / 2))
        factors += number
        return factors.sortedArray()
    }
}

open class Car {
    open fun getName(): String {
        return "car"
    }
}

class Sedan : Car() {
    override fun getName(): String {
        return "sedan"
    }
}

class Suv : Car() {
    override fun getName(): String {
        return "suv"
    }
}