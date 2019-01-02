package br.com.omniatechnology.verificaresultado;

import java.util.ArrayList;
import java.util.List;

public class Resultado {

	private Integer numeroJogo;
	private Integer totalAcertos;
	private List<Integer> valores;
	
	public Resultado() {
		valores = new ArrayList<Integer>();
	}

	public Integer getNumeroJogo() {
		return numeroJogo;
	}

	public void setNumeroJogo(Integer numeroJogo) {
		this.numeroJogo = numeroJogo;
	}

	public List<Integer> getValores() {
		return valores;
	}

	public void setValores(List<Integer> valores) {
		this.valores = valores;
	}

	public Integer getTotalAcertos() {
		return totalAcertos;
	}

	public void setTotalAcertos(Integer totalAcertos) {
		this.totalAcertos = totalAcertos;
	}

	@Override
	public String toString() {
		return "[numeroJogo=" + numeroJogo + ", Acertos=" + totalAcertos + ",\n valores=" + valores + "]";
	}
	
}
