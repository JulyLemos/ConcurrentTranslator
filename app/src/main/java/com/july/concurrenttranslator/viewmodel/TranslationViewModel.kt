package com.july.concurrenttranslator.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.july.concurrenttranslator.data.repository.TranslationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

sealed class TranslationState {
    object Loading: TranslationState()
    data class Success(val translatedText: String) : TranslationState()
    data class Error(val message: String) : TranslationState()
}

class TranslationViewModel : ViewModel() {
    private val repository = TranslationRepository()
    private val _translationState = MutableLiveData<TranslationState>()

    val translationState: LiveData<TranslationState> = _translationState

    fun translate(sourceLang: String, targetLang: String, inputText: String){
        if (inputText.isBlank()) {
            _translationState.value = TranslationState.Error("Please, write something")
            return
        }

        viewModelScope.launch(Dispatchers.IO){
            _translationState.postValue(TranslationState.Loading)

            val result = repository.translate(sourceLang, targetLang, inputText)

            result.fold(
                onSuccess = { translatedText ->
                    _translationState.postValue(TranslationState.Success(translatedText))
                },
                onFailure = { exception ->
                    _translationState.postValue(
                        TranslationState.Error("Error to translate: ${exception.message}")
                    )
                }
            )
        }
    }
}