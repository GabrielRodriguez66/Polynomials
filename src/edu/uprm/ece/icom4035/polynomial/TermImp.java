package edu.uprm.ece.icom4035.polynomial;

public class TermImp implements Term {
	private double coefficient;
	private int exponent;
	
	public TermImp(String term) {
		if(term.contains("x^")) {
			
			if(term.startsWith("x")) this.setCoefficient(1);
			else this.setCoefficient(Double.valueOf(term.substring(0, term.indexOf("x"))));
		
			this.setExponent(Integer.valueOf(term.substring(term.indexOf("^")+1,term.length())));

		}else if(term.contains("x")) {
			
			this.setExponent(1);
			if(term.startsWith("x")) this.setCoefficient(1);
			else {
				this.setCoefficient(Double.valueOf(term.substring(0, term.indexOf("x"))));
			}
		}else {
			this.setExponent(0);
			this.setCoefficient(Double.valueOf(term));
		}
	}

	public TermImp(Double c, int e) {
		this.coefficient = c;
		this.exponent = e;
	}
	
	@Override
	public double evaluate(double x) {
		return this.coefficient*Math.pow(x, this.exponent);
	}
	
	@Override
	public boolean equals(Term T2) {
		return this.getCoefficient() == T2.getCoefficient() && this.getExponent() == T2.getExponent();
	}

	@Override
	public double getCoefficient() {
		return coefficient;
	}

	@Override
	public int getExponent() {
		return exponent;
	}

	public void setCoefficient(double coefficient) {
		this.coefficient = coefficient;
	}

	public void setExponent(int exponent) {
		this.exponent = exponent;
	}
	
	@Override
	public String toString() {
		if(this.getExponent() == 0) return String.format("%.2f", this.getCoefficient())+"";
		StringBuilder str = new StringBuilder();

		if(this.getExponent() == 1) str.append(String.format("%.2f", this.getCoefficient())+"x");
		else str.append(String.format("%.2f", this.getCoefficient())+"x^"+this.getExponent());
		
		return str.toString();
	}

}
