package br.com.omniatechnology.verificaresultado;

import java.util.ArrayList;
import java.util.List;

public class Result {

	private Integer number;
	private Integer totalWin;
	private List<Integer> values;
	
	public Result() {
		values = new ArrayList<Integer>();
	}

	public Integer getNumber() {
		return number;
	}

	public void setNumber(Integer number) {
		this.number = number;
	}

	public List<Integer> getValues() {
		return values;
	}

	public void setValues(List<Integer> values) {
		this.values = values;
	}

	public Integer getTotalWin() {
		return totalWin;
	}

	public void setTotalWin(Integer totalWin) {
		this.totalWin = totalWin;
	}

	@Override
	public String toString() {
		return "[numeroJogo=" + number + ", Acertos=" + totalWin + ",\n valores=" + values + "]";
	}
}
