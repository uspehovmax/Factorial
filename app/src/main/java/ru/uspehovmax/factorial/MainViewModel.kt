package ru.uspehovmax.factorial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import java.math.BigInteger
import kotlin.concurrent.thread
import kotlin.coroutines.suspendCoroutine

/**
 * Архитектура  UDF.
 * Реализация когда в Одной переменной (экз.класса) хранится состояние экрана.
 * Через класс State (...), где параметры,определяют сосотяние.
 *
 * CoroutineScope содержит одно поле CoroutineContext. CoroutineContext - набор из 4 элементов:
 * 1 - поток (Dispatcher), на котором выполняется корутина
 * 2 - объект Job
 * 3 - ExceptionHandler - обработка ошибок
 * 4 - название коротины CoroutineName
 * в блоке withContext() передаем контекст этом ожет быть несколько элементов через +
 *
 */
class MainViewModel : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    private val myCoroutineScope = CoroutineScope(Dispatchers.Main + Job() + CoroutineName("My Coroutine Name"))

    fun calculate(value: String?) {
        _state.value = Progress
        if (value.isNullOrBlank()) {
            _state.value = Error
            return
        }

        myCoroutineScope.launch()
        {
            val number = value.toLong()
            val result = withContext(Dispatchers.Default) {
                factorial(number)
            }
            _state.value = Factorial(result)
        }

/*        viewModelScope.launch {
            val number = value.toLong()
/*             calculate
            delay(2_000)
            val factor = factorial(number)
            _state.value = Factorial(factor*//*.toString()*//*)*/
            val result = withContext(Dispatchers.Default) {
                factorial(number)
            }
            _state.value = Factorial(result)
        }*/
    }

    private suspend fun factorial(number: Long): String {
        //1  без корутин - приложение зависает
//        val number = value.toLong()
        var result = BigInteger.ONE
        for (i in 1..number) {
            result = result.multiply(BigInteger.valueOf(i))
        }
        return result.toString()

//        2  с корутинами - suspendCoroutine {... resumeWith(..) } реализация с колбеком -
//        выполняется в отдельном потоке и возвращает результат. Вызов resumeWith() - обязателен!
/*        return suspendCoroutine {
            thread {
                var result = BigInteger.ONE
                for (i in 1..number) {
                    result = result.multiply(BigInteger.valueOf(i))
                }
                it.resumeWith(Result.success(result.toString()))
            }
        }*/

        //3 с корутинами, предпочтит.  - withContext(), Dispatchers.Default - выполнение в потоках = кол-ву процов
        //  withContext() позволяет перекл. потоки, принимает к кач.параметра CoroutineContext, поток,
        //  на котором будет выполняться задача
/*        return withContext(Dispatchers.Default) {
                var result = BigInteger.ONE
                for (i in 1..number) {
                    result = result.multiply(BigInteger.valueOf(i))
                }
                result.toString()
        }*/
    }

    override fun onCleared() {
        super.onCleared()
        myCoroutineScope.cancel()
    }
}
