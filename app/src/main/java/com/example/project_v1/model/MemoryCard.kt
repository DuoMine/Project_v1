package com.eunly00.MachTheTanghuru.models

data class MemoryCard( // class object - constructors use this for each card in the game
    val identifier: Int,
    var isFaceUp: Boolean = false, // changes from up to down so must be var
    var isMatched: Boolean = false
)