package br.com.omniatechnology.verificaresultado;

import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class CheckLottery {

    public static List<Result> checkLottery(
            BufferedReader lerArq,
            String valoresPremiados,
            Integer quantidadeDeValoresACalcular,
            Context context
    ) throws IOException {

        LoteriaJogo lotoFacil = new LoteriaJogo();
        Lottery lotteryPremiado = replaceLottery(
                valoresPremiados.replace("[", "").replace("]", ""),
                context
        );

        if (lotteryPremiado.getNumbersLottery().size() < quantidadeDeValoresACalcular) {
            return null;
        }

        Lottery lottery;
        String linha = lerArq.readLine();
        while (linha != null)

        {

            linha = linha.replace("[", "").replace("]", "");

            if(!linha.isEmpty()) {

                String[] values = linha.split(Pattern.quote("|"));
                Integer numbers = Integer.parseInt(values[0].trim());

                lottery = replaceLottery(values[1].trim(), context);
                lottery.setNumber(numbers);
                lotoFacil.getJogos().add(lottery);
            }

            linha = lerArq.readLine();
        }

        lerArq.close();
//        arq.close();

        Iterator<Lottery> jogos = lotoFacil.getJogos().iterator();
        int total = 0;
        List<Result> results = new ArrayList<Result>();
        while (jogos.hasNext())

        {
            Lottery lotteryTemp = jogos.next();
            if (verificarPremiacao(quantidadeDeValoresACalcular, lotteryPremiado, lotteryTemp, results))
                total++;
        }

        //exportar(resultados);

        return results;
    }

    private static boolean verificarPremiacao(Integer quantidadeDeValoresACalcular, Lottery lotteryPremiado, Lottery lottery,
                                              List<Result> results) {

        Result result = new Result();

        int contador = 0;
        Iterator<Integer> valoresPremiados = lotteryPremiado.getNumbersLottery().iterator();
        while (valoresPremiados.hasNext()) {
            Integer valor = valoresPremiados.next();
            Iterator<Integer> valoresJogos = lottery.getNumbersLottery().iterator();
            while (valoresJogos.hasNext()) {
                Integer valorTemp = valoresJogos.next();
                if (valorTemp == valor) {
                    contador++;
                    result.getValues().add(valor);
                    break;
                }
            }
        }

        if (contador < quantidadeDeValoresACalcular) {
            return false;
        } else {
            result.setNumber(lottery.getNumber());
            result.setTotalWin(contador);
            results.add(result);
            return true;
        }
    }

    private static Lottery replaceLottery(
            String linePlaysValuesNoFormatted,
            Context context
    ) {
        Lottery lottery = new Lottery();
        String[] linePlaysValuesFormatted = new String[0];

        try {
            linePlaysValuesFormatted = linePlaysValuesNoFormatted.split(Pattern.quote(","));

            for (String lineValue : linePlaysValuesFormatted) {
                lottery.getNumbersLottery().add(Integer.parseInt(lineValue.trim()));
            }
        }catch (Exception ex) {
            Toast.makeText(context,
                    "Erro ao processar valores " + Arrays.toString(linePlaysValuesFormatted),
                    Toast.LENGTH_LONG).show();
        }
        return lottery;
    }

    public static String gerarString(List<Result> results){

        StringBuilder retorno = new StringBuilder();
        retorno.append("RESULTADOS: \n\n");

        for (Result r : results) {
            retorno.append(r.toString());
            retorno.append("\r\n");
        }

        retorno.append("\r\n");
        retorno.append("Total: " + results.size());

        return retorno.toString();
    }
}
