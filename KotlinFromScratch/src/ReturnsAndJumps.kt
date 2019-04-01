import kotlin.math.absoluteValue

class ReturnsAndJumps {

    private val obstacles = arrayOf(Pair(1, 1), Pair(2, 0), Pair(1, -5))


    fun test() {
        println("Break and Continue Labels")
        val position = Pair(0, 0)
        move1(position, Pair(3, -5))
        move2(position, Pair(3, -5))
    }

    private fun move1(position: Pair<Int, Int>, steps: Pair<Int, Int>): Pair<Int, Int> {
        var newPos = position
        val moveForward = steps.first > 0
        val turnRight = steps.second > 0

        val x = if (moveForward) 1 else -1
        val y = if (turnRight) 1 else -1

        moving@ while (true) {
            horizontal@ for (upOrDown in 1..steps.first.absoluteValue) {
                newPos = Pair(newPos.first + x, newPos.second)
                print("$newPos")
            }


            vertical@ for (leftOrRight in 1..steps.second.absoluteValue) {
                newPos = Pair(newPos.first, newPos.second + y)
                print("$newPos")
            }
            break@moving
        }
        println("")
        return newPos
    }

    private fun move2(position: Pair<Int, Int>, steps: Pair<Int, Int>): Pair<Int, Int> {
        var newPos = position
        val moveForward = steps.first > 0
        val turnRight = steps.second > 0

        val x = if (moveForward) 1 else -1
        val y = if (turnRight) 1 else -1

        moving@ while (true) {
            horizontal@ for (upOrDown in 1..steps.first.absoluteValue) {
                if (hasObstacle(Pair(newPos.first + x, newPos.second))) {
                    print("Oops,${Pair(newPos.first + x, newPos.second)} is not allowed")
                    break@horizontal
                }
                newPos = Pair(newPos.first + x, newPos.second)
                print("$newPos")
            }


            vertical@ for (leftOrRight in 1..steps.second.absoluteValue) {
                if (hasObstacle(Pair(newPos.first, newPos.second + y))) {
                    print("Oops,${Pair(newPos.first, newPos.second + y)} is not allowed")
                    break@vertical
                }
                newPos = Pair(newPos.first, newPos.second + y)
                print("$newPos")
            }
            break@moving
        }
        println("")
        return newPos
    }

    private fun hasObstacle(pos: Pair<Int, Int>): Boolean {
        obstacles.forEach {
            if (it.first == pos.first && it.second == pos.second) {
                return true
            }
        }
        return false
    }
}