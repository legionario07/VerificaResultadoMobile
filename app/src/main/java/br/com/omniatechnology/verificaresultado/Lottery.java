package br.com.omniatechnology.verificaresultado;

import java.util.SortedSet;
import java.util.TreeSet;

public class Lottery {

	private Integer number;
	private SortedSet<Integer> numbersLottery;

	public Lottery() {
		numbersLottery = new TreeSet<>();
	}
	
	public SortedSet<Integer> getNumbersLottery() {
		return numbersLottery;
	}

	public void setNumbersLottery(SortedSet<Integer> numbersLottery) {
		this.numbersLottery = numbersLottery;
	}

	@Override
	public String toString() {
		return number + "|" + numbersLottery;
	}


	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((numbersLottery == null) ? 0 : numbersLottery.hashCode());
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
		Lottery other = (Lottery) obj;
		if (numbersLottery == null) {
			if (other.numbersLottery != null)
				return false;
		} else if (!numbersLottery.equals(other.numbersLottery))
			return false;
		return true;
	}
}
