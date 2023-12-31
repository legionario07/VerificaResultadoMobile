package br.com.omniatechnology.verificaresultado

class LotteryGame {

    var playedNumbers = mutableSetOf<Lottery>()

    override fun toString(): String {
        return "LotoFacil [jogos=$playedNumbers]"
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + playedNumbers.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        val otherValue = other as LotteryGame
        if (playedNumbers != otherValue.playedNumbers) return false
        return true
    }
}
