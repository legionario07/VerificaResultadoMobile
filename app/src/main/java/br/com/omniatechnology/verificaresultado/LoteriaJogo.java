package br.com.omniatechnology.verificaresultado;

import java.util.HashSet;
import java.util.Set;

public class LoteriaJogo {

	private Set<Jogo> jogos;
	
	public LoteriaJogo() {
		jogos = new HashSet<Jogo>();
	}

	public Set<Jogo> getJogos() {
		return jogos;
	}

	public void setJogos(Set<Jogo> jogos) {
		this.jogos = jogos;
	}

	@Override
	public String toString() {
		return "LotoFacil [jogos=" + jogos + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((jogos == null) ? 0 : jogos.hashCode());
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
		if (jogos == null) {
			if (other.jogos != null)
				return false;
		} else if (!jogos.equals(other.jogos))
			return false;
		return true;
	}

	
	
}
