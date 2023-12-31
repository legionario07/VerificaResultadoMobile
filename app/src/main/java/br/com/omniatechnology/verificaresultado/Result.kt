package br.com.omniatechnology.verificaresultado

class Result {
	var number: Int? = null
	var totalWin: Int? = null
	var values: MutableList<Int> = mutableListOf()

    override fun toString(): String {
        return "[numeroJogo=$number, Acertos=$totalWin,\n valores=$values]"
    }
}
