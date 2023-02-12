package ru.uspehovmax.factorial

/*class State(
    val isError: Boolean = false,
    val isInProgress: Boolean = false,
    val factorial: String = ""
)*/
/**
 * Sealed class - при компиляции извесстны все классы наследникики
 * При использовании when() - проверка перечисления всех наследников
 * */

sealed class State

object Error: State()
object Progress: State()
class Factorial(val value: String): State()