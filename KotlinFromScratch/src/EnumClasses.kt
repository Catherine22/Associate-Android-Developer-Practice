class EnumClasses {
    enum class Direction {
        NORTH, SOUTH, WEST, EAST
    }

    enum class Color(val rgb: Int) {
        RED(0xFF0000),
        GREEN(0x00FF00),
        BLUE(0x0000FF)
    }

    enum class ProtocolState {
        WAITING {
            override val id: Int
                get() = 1

            override fun signal(): ProtocolState = WAITING
        },

        TALKING {
            override val id: Int
                get() = 2

            override fun signal(): ProtocolState = TALKING
        };

        abstract val id: Int
        abstract fun signal(): ProtocolState
    }

    enum class RGB { RED, GREEN, BLUE }

    inline fun <reified T : Enum<T>> printAllValues() {
        print(enumValues<T>().joinToString { it.name })
    }

    fun test() {
        println("enum 1") // Basic usage of enum classes
        println("Direction.NORTH = ${Direction.NORTH}")


        println("")
        println("enum 2") // Another way to initialse the enum
        println("Color = ${Color.RED}(${Integer.toHexString(Color.RED.rgb)}")

        println("")
        println("Anonymous Classes") // Enum constants can also declare their own anonymous classes
        println("ProtocolState = ${ProtocolState.TALKING.signal()}(id = ${ProtocolState.TALKING.id})")

        println("")
        println("Enum Constants")
        printAllValues<RGB>()
    }
}