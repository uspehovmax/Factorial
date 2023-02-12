package ru.uspehovmax.factorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import ru.uspehovmax.factorial.databinding.ActivityMainBinding

/**
 * Архитектура  UDF.
 * Реализация когда в Одной переменной (экз.класса) хранится состояние экрана.
 * Через класс State (...), где параметры,определяют сосотяние.
 *
 */
class MainActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val viewModel by lazy {
        ViewModelProvider(this)[MainViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observeViewModel()

        with(binding) {
            buttonCalculate.setOnClickListener {
                viewModel.calculate(editTextNumber.text.toString())
            }
        }
    }

    private fun observeViewModel() {

    // реализация через класс Sate()
/*        viewModel.state.observe(this) {
            if (it.isError) {
                Toast.makeText(
                    this,
                    "You did not entered value",
                    Toast.LENGTH_SHORT
                ).show()
            }
            if (it.isInProgress) {
                binding.progressBarLoading.visibility = View.VISIBLE
                binding.buttonCalculate.isEnabled = false
            } else {
                binding.progressBarLoading.visibility = View.GONE
                binding.buttonCalculate.isEnabled = true
            }

            binding.textViewFactorial.text = it.factorial
        }*/

        // реализация через sealed class Sate()
        viewModel.state.observe(this) {
            Log.d("MSG", "observing ")
            binding.progressBarLoading.visibility = View.GONE
            binding.buttonCalculate.isEnabled = true
            when(it) {
                is Error -> {
                    Toast.makeText(
                        this,
                        "You did not entered value",
                        Toast.LENGTH_SHORT
                    ).show()
                    Log.d("MSG", "is Error ")
                }

                is Progress -> {
                    binding.progressBarLoading.visibility = View.VISIBLE
                    binding.buttonCalculate.isEnabled = false
                    Log.d("MSG", "is Progress ")

                }

                is Factorial -> {
                    binding.textViewFactorial.text = it.value
                    Log.d("MSG", "is Result: ${it.value} ")

                }
            }
        }

    }
}