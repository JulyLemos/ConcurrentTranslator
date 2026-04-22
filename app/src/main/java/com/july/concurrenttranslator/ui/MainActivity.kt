package com.july.concurrenttranslator.ui

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.july.concurrenttranslator.data.model.Language
import com.july.concurrenttranslator.databinding.ActivityMainBinding
import com.july.concurrenttranslator.viewmodel.TranslationState
import com.july.concurrenttranslator.viewmodel.TranslationViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: TranslationViewModel by viewModels()
    private var languages: List<Language> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        languages = loadLanguagesFromAssets()
        setupSpinners()
        setupButton()
        observeViewModel()
    }

    private fun loadLanguagesFromAssets(): List<Language> {
        val jsonString = assets.open("languages.json")
            .bufferedReader()
            .use { it.readText() }
        val type = object : TypeToken<Map<String, List<Language>>>() {}.type
        val map: Map<String, List<Language>> = Gson().fromJson(jsonString, type)
        return map["languages"] ?: emptyList()
    }

    private fun setupSpinners() {
        val languageNames = languages.map { it.name }
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, languageNames)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerSource.adapter = adapter
        binding.spinnerTarget.adapter = adapter
        binding.spinnerSource.setSelection(languages.indexOfFirst { it.code == "pt" })
        binding.spinnerTarget.setSelection(languages.indexOfFirst { it.code == "en" })
    }

    private fun setupButton() {
        binding.btnTranslate.setOnClickListener {
            val inputText = binding.etInputText.text.toString()
            val sourceLang = languages[binding.spinnerSource.selectedItemPosition].code
            val targetLang = languages[binding.spinnerTarget.selectedItemPosition].code
            viewModel.translate(sourceLang, targetLang, inputText)
        }
    }

    private fun observeViewModel() {
        viewModel.translationState.observe(this) { state ->
            when (state) {
                is TranslationState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.tvResult.visibility = View.GONE
                    binding.btnTranslate.isEnabled = false
                }
                is TranslationState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvResult.visibility = View.VISIBLE
                    binding.tvResult.text = state.translatedText
                    binding.btnTranslate.isEnabled = true
                }
                is TranslationState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.tvResult.visibility = View.VISIBLE
                    binding.tvResult.text = "❌ ${state.message}"
                    binding.btnTranslate.isEnabled = true
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}