class Generics {
    interface Source<out T> {
        fun nextT(): T
    }

    private fun demo(source: Source<String>) {
        // Declaration-site variance
        val objects: Source<Any> = source
        println(objects.nextT())
    }

    class MyArray<T> {
        var array: Array<T>? = null
        var size: Int = 0
        var indices = 0..0
        fun get(index: Int): T = array?.get(index) ?: throw ArrayIndexOutOfBoundsException()
        fun set(index: Int, value: T) {
            array?.set(index, value)
            size = array?.size ?: 0
            indices = 0..size
        }

//        fun copy(from: MyArray<T>, to: MyArray<T>) {
//            assert(from.size == to.size)
//            for (i in from.indices)
//                to.set(i, from.get(i))
//        }

        // solution 1
        fun copy(from: MyArray<out T>, to: MyArray<T>) {
            assert(from.size == to.size)
            for (i in from.indices)
                to.set(i, from.get(i))
        }

        // solution 2
//        fun copy(from: MyArray<in T>, to: MyArray<T>) {
//            assert(from.size == to.size)
//            for (i in from.indices)
//                to.set(i, from.get(i) as T)
//        }
    }

    fun test() {
        println("Declaration-site variance")
        // Declaration-site variance -> In Java, we can't declare type String to type Object
        demo(object : Source<String> {
            override fun nextT(): String {
                return "next"
            }
        })


        println("")
        println("Type projections")

        val ints = MyArray<Int>()
        for (i in 0..5) {
            ints.set(i, i)
        }

        val anys: MyArray<Any> = MyArray<Any>()
        anys.copy(ints, anys)
        // Type mismatch: require Array<Any>, found Array<Int>
        // To solve this, we have 2 solutions:
        // 1. Add "out" -> from: MyArray<out T>
        // 2. Add "in" -> from:




    }
}
