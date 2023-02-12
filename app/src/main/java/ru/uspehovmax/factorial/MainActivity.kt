package ru.uspehovmax.factorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.uspehovmax.factorial.databinding.ActivityMainBinding

/**
 * Архитектура  UDF.
 * Реализация через State (...).
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

        viewModel.state.observe(this) {
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
        }
    }
}