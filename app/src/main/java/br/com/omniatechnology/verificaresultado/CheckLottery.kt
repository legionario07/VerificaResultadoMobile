package br.com.omniatechnology.verificaresultado

import android.content.Context
import android.widget.Toast
import java.io.BufferedReader
import java.io.IOException
import java.util.Arrays
import java.util.regex.Pattern

object CheckLottery {

    @Throws(IOException::class)
    fun checkLottery(
        readFile: BufferedReader,
        numbersWins: String,
        minNumberForWins: Int,
        context: Context
    ): List<Result> {
        val lotoFacil = LotteryGame()
        val lotteryPremiado = replaceLottery(
            numbersWins.replace("[", "").replace("]", ""),
            context
        )
        if (lotteryPremiado.numbersLottery!!.size < minNumberForWins) {
            return mutableListOf()
        }
        var lottery: Lottery
        var linha = readFile.readLine()
        while (linha != null) {
            linha = linha.replace("[", "").replace("]", "")
            if (linha.isNotEmpty()) {
                val values =
                    linha.split(Pattern.quote("|").toRegex()).dropLastWhile { it.isEmpty() }
                        .toTypedArray()
                val numbers = values[0].trim { it <= ' ' }.toInt()
                lottery = replaceLottery(values[1].trim { it <= ' ' }, context)
                lottery.number = numbers
                lotoFacil.playedNumbers.add(lottery)
            }
            linha = readFile.readLine()
        }
        readFile.close()
        //        arq.close();
        val jogos: Iterator<Lottery> = lotoFacil.playedNumbers.iterator()
        var total = 0
        val results: MutableList<Result> = ArrayList()
        while (jogos.hasNext()) {
            val lotteryTemp = jogos.next()
            if (verificarPremiacao(
                    minNumberForWins,
                    lotteryPremiado,
                    lotteryTemp,
                    results
                )
            ) total++
        }

        //exportar(resultados);
        return results
    }

    private fun verificarPremiacao(
        quantidadeDeValoresACalcular: Int, lotteryPremiado: Lottery, lottery: Lottery,
        results: MutableList<Result>
    ): Boolean {
        val result = Result()
        var contador = 0
        val valoresPremiados: Iterator<Int> = lotteryPremiado.numbersLottery!!.iterator()
        while (valoresPremiados.hasNext()) {
            val valor = valoresPremiados.next()
            val valoresJogos: Iterator<Int> = lottery.numbersLottery!!.iterator()
            while (valoresJogos.hasNext()) {
                val valorTemp = valoresJogos.next()
                if (valorTemp == valor) {
                    contador++
                    result.values.add(valor)
                    break
                }
            }
        }
        return if (contador < quantidadeDeValoresACalcular) {
            false
        } else {
            result.number = lottery.number
            result.totalWin = contador
            results.add(result)
            true
        }
    }

    private fun replaceLottery(
        linePlaysValuesNoFormatted: String,
        context: Context
    ): Lottery {
        val lottery = Lottery()
        var linePlaysValuesFormatted = arrayOfNulls<String>(0)
        try {
            linePlaysValuesFormatted =
                linePlaysValuesNoFormatted.split(Pattern.quote(",").toRegex())
                    .dropLastWhile { it.isEmpty() }
                    .toTypedArray()
            for (lineValue in linePlaysValuesFormatted) {
                lottery.numbersLottery!!.add(lineValue!!.trim { it <= ' ' }.toInt())
            }
        } catch (ex: Exception) {
            Toast.makeText(
                context,
                "Erro ao processar valores " + linePlaysValuesFormatted.contentToString(),
                Toast.LENGTH_LONG
            ).show()
        }
        return lottery
    }

    fun createDataWithResults(results: List<Result>): String {
        val retorno = StringBuilder()
        retorno.append("RESULTADOS: \n\n")
        for (r in results) {
            retorno.append(r.toString())
            retorno.append("\r\n")
        }
        retorno.append("\r\n")
        retorno.append("Total: " + results.size)
        return retorno.toString()
    }
}