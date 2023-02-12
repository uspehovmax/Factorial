package ru.uspehovmax.factorial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigInteger
import kotlin.concurrent.thread
import kotlin.coroutines.suspendCoroutine

/**
 * Архитектура  UDF.
 * Реализация когда в Одной переменной (экз.класса) хранится состояние экрана.
 * Через класс State (...), где параметры,определяют сосотяние.
 *
 */
class MainViewModel : ViewModel() {

    private val _state = MutableLiveData<State>()
    val state: LiveData<State>
        get() = _state

    fun calculate(value: String?) {
        _state.value = Progress
        if (value.isNullOrBlank()) {
            _state.value = Error
            return
        }

        viewModelScope.launch {
            val number = value.toLong()
            // calculate
            //delay(2_000)
            val factor = factorial(number)

            _state.value = Factorial(factor/*.toString()*/)
        }
    }

    private suspend fun factorial(number: Long): String {
        //1  без корутин - приложение зависает
/*        val number = value.toLong()
            var result = BigInteger.ONE
        for (i in 1..number) {
            result = result.multiply(BigInteger.valueOf(i))
        }
        return result*/

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
        return withContext(Dispatchers.Default) {
                var result = BigInteger.ONE
                for (i in 1..number) {
                    result = result.multiply(BigInteger.valueOf(i))
                }
                result.toString()
        }
    }
}
