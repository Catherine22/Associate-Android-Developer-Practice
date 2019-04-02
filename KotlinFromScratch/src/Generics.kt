class Generics {
    interface Source<out T> {
        fun nextT(): T
    }

    private fun demo(source: Source<String>) {
        // Declaration-site variance
        val objects: Source<Any> = source
        println(objects.nextT())
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
    }
}
