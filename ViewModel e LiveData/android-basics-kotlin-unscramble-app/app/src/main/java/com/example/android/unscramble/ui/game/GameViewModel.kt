package com.example.android.unscramble.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GameViewModel : ViewModel() {


    private var wordsList: MutableList<String> = mutableListOf()

    private val _currentScrambledWord = MutableLiveData<String>()



    //criando uma variável que pode ser acessada de fora
    //sobrescrevendo seu método get apontando para a variável interna
    //como ela é somente leitura, ela nao pode ser alterada de fora, apenas lida

    //mutable live data é val porque a variável se mantem, só os dados armazenados que mudam
    private val _score = MutableLiveData(0) //nao precisa passar o tipo se já inferir
    val score: LiveData<Int>
        get() = _score

    private val _currentWordCount = MutableLiveData(0)
    val currentWordCount: LiveData<Int>
        get() = _currentWordCount

    private lateinit var _currentWord: String
    val currentWord: String
        get() = _currentWord

    val currentScrambledWord: LiveData<String> //live data pq nao muda
        get() = _currentScrambledWord


    init {
        getNextWord()
    }

    private fun getNextWord() {
        _currentWord = allWordsList.random()
        //um array tem tamanho fixo, um List não! já quem o add() e o remove()
        val tempWord = _currentWord.toCharArray()
        tempWord.shuffle()

        while (tempWord.toString().equals(_currentWord, false)) {
            tempWord.shuffle()
        }

        if (wordsList.contains((_currentWord))) {
            getNextWord()
        } else {
            // .value pq é mutablelivedata e pode mudar seu conteudo armazenado
            _currentScrambledWord.value = String(tempWord)
            _currentWordCount.value = (_currentWordCount.value)?.inc() //incrementa 1 unidade, null safety
            wordsList.add(_currentWord)
        }
    }

    fun nextWord(): Boolean {
        return if (_currentWordCount.value!! < MAX_NO_OF_WORDS) {
            getNextWord()
            true
        } else {
            false
        }
    }

    private fun increaseScore() {
        _score.value = (_score.value)?.plus(SCORE_INCREASE)  //adiciona o score, null safety
    }

    fun isUserCorrect(playerWord: String): Boolean {
        if (playerWord.equals(_currentWord, true)) {
            increaseScore()
            return true
        }
        return false
    }


    fun reinitializeData() {
        _score.value = 0
        _currentWordCount.value = 0
        wordsList.clear()
        getNextWord()
    }


}