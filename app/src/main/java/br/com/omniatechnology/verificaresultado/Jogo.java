package br.com.omniatechnology.verificaresultado;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class Jogo  {

	
	private Integer numeroJogo;
	
	private SortedSet<Integer> numeros;

	public Jogo() {
		numeros = new TreeSet<>();
	}
	
	public SortedSet<Integer> getNumeros() {
		return numeros;
	}

	public void setNumeros(SortedSet<Integer> numeros) {
		this.numeros = numeros;
	}

	@Override
	public String toString() {
		return numeroJogo + "|" + numeros;
	}


	public Integer getNumeroJogo() {
		return numeroJogo;
	}

	public void setNumeroJogo(Integer numeroJogo) {
		this.numeroJogo = numeroJogo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numeros == null) ? 0 : numeros.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Jogo other = (Jogo) obj;
		if (numeros == null) {
			if (other.numeros != null)
				return false;
		} else if (!numeros.equals(other.numeros))
			return false;
		return true;
	}

	
	
}
