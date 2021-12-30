package br.com.omniatechnology.verificaresultado;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

public class VerificaJogo {

    public static List<Resultado>  verificar(BufferedReader lerArq, String valoresPremiados, Integer quantidadeDeValoresACalcular) throws IOException {

        LoteriaJogo lotoFacil = new LoteriaJogo();
        Jogo jogoPremiado = new Jogo();

        jogoPremiado = replaceJogo(valoresPremiados.replace("[", "").replace("]", ""));

        if (jogoPremiado.getNumeros().size() < quantidadeDeValoresACalcular) {
            return null;
        }

        Jogo jogo = null;
        String linha = lerArq.readLine();
        while (linha != null)

        {
            jogo = new Jogo();

            linha = linha.replace("[", "").replace("]", "");
            String[] valores = linha.split(Pattern.quote("|"));
            Integer numeroJogo = Integer.parseInt(valores[0].trim());

            jogo = replaceJogo(valores[1]);
            jogo.setNumeroJogo(numeroJogo);
            lotoFacil.getJogos().add(jogo);

            linha = lerArq.readLine();
        }

        lerArq.close();
//        arq.close();

        Iterator<Jogo> jogos = lotoFacil.getJogos().iterator();
        int total = 0;
        List<Resultado> resultados = new ArrayList<Resultado>();
        while (jogos.hasNext())

        {
            Jogo jogoTemp = jogos.next();
            if (verificarPremiacao(quantidadeDeValoresACalcular, jogoPremiado, jogoTemp, resultados))
                total++;
        }

        //exportar(resultados);

        return resultados;
    }

    private static boolean verificarPremiacao(Integer quantidadeDeValoresACalcular, Jogo jogoPremiado, Jogo jogo,
                                              List<Resultado> resultados) {

        Resultado resultado = new Resultado();

        int contador = 0;
        Iterator<Integer> valoresPremiados = jogoPremiado.getNumeros().iterator();
        while (valoresPremiados.hasNext()) {
            Integer valor = valoresPremiados.next();
            Iterator<Integer> valoresJogos = jogo.getNumeros().iterator();
            while (valoresJogos.hasNext()) {
                Integer valorTemp = valoresJogos.next();
                if (valorTemp == valor) {
                    contador++;
                    resultado.getValores().add(valor);
                    break;
                }
            }
        }

        if (contador < quantidadeDeValoresACalcular) {
            return false;
        } else {
            resultado.setNumeroJogo(jogo.getNumeroJogo());
            resultado.setTotalAcertos(contador);
            resultados.add(resultado);
            return true;
        }
    }

    private static Jogo replaceJogo(String textoSemFormato) {
        Jogo jogo = new Jogo();

        String[] valoresJogos = textoSemFormato.split(Pattern.quote(","));

        for (int i = 0; i < valoresJogos.length; i++) {
            jogo.getNumeros().add(Integer.parseInt(valoresJogos[i].trim()));
        }

        return jogo;
    }

    public static String gerarString(List<Resultado> resultados){

        StringBuilder retorno = new StringBuilder();
        retorno.append("RESULTADOS: \n\n");

        for (Resultado r : resultados) {
            retorno.append(r.toString());
            retorno.append("\r\n");
        }

        retorno.append("\r\n");
        retorno.append("Total: " + resultados.size());

        return retorno.toString();
    }
}
