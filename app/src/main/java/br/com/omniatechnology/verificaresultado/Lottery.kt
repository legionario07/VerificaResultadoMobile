package br.com.omniatechnology.verificaresultado

import java.util.SortedSet
import java.util.TreeSet

class Lottery {
    @JvmField
	var number: Int? = null
    @JvmField
	var numbersLottery: SortedSet<Int>? = TreeSet()

    override fun toString(): String {
        return number.toString() + "|" + numbersLottery
    }

    override fun hashCode(): Int {
        val prime = 31
        var result = 1
        result = prime * result + if (numbersLottery == null) 0 else numbersLottery.hashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null) return false
        if (javaClass != other.javaClass) return false
        val otherValue = other as Lottery
        if (numbersLottery == null) {
            if (otherValue.numbersLottery != null) return false
        } else if (numbersLottery != otherValue.numbersLottery) return false
        return true
    }
}