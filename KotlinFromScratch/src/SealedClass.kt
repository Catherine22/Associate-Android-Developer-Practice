class SealedClass {
    fun test() {

        println("enum")
        val state = EnumState.PLAYING
        println(getStateDescription(state))

        println("")
        println("Replaced with sealed class")
        val playerA = SealedState.Play("A")
        println(getSealedStateDescription(playerA))
        val defaultPlayer = SealedState.PLAYING_
        println(getSealedStateDescription(defaultPlayer))
    }

    private fun getStateDescription(state: EnumState) = when (state) {
        EnumState.PAUSE -> "Player is paused"
        EnumState.IDLE -> "Player is idle"
        EnumState.PLAYING -> "Player is playing"
        EnumState.STOP -> "Player is stopped"
    }

    private fun getSealedStateDescription(state: SealedState) = when (state) {
        SealedState.PAUSE_ -> "Player is paused"
        SealedState.IDLE_ -> "Player is idle"
        SealedState.PLAYING_ -> "Player is playing"
        SealedState.STOP_ -> "Player is stopped"
        is SealedState.Pause -> "Player ${state.name} is paused"
        is SealedState.Play -> "Player ${state.name} is playing"
        is SealedState.Idle -> "Player ${state.name} is idle"
        is SealedState.Stop -> "Player ${state.name} is stopped"
    }
}

enum class EnumState {
    PAUSE, PLAYING, IDLE, STOP
}

sealed class SealedState {
    data class Pause(val name: String) : SealedState()
    data class Play(val name: String) : SealedState()
    data class Idle(val name: String) : SealedState()
    data class Stop(val name: String) : SealedState()
    object PAUSE_ : SealedState()
    object PLAYING_ : SealedState()
    object IDLE_ : SealedState()
    object STOP_ : SealedState()
}