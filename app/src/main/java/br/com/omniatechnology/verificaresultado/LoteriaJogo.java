package br.com.omniatechnology.verificaresultado;

import java.util.HashSet;
import java.util.Set;

public class LoteriaJogo {

	private Set<Lottery> lotteries;
	
	public LoteriaJogo() {
		lotteries = new HashSet<Lottery>();
	}

	public Set<Lottery> getJogos() {
		return lotteries;
	}

	public void setJogos(Set<Lottery> lotteries) {
		this.lotteries = lotteries;
	}

	@Override
	public String toString() {
		return "LotoFacil [jogos=" + lotteries + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((lotteries == null) ? 0 : lotteries.hashCode());
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
		LoteriaJogo other = (LoteriaJogo) obj;
		if (lotteries == null) {
			if (other.lotteries != null)
				return false;
		} else if (!lotteries.equals(other.lotteries))
			return false;
		return true;
	}

	
	
}
