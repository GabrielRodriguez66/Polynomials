package edu.uprm.ece.icom4035.polynomial;

public class TermImp implements Term {
	private double coefficient;
	private int exponent;

	/**
	 * This constructor can take a polynomial term in String form
	 * and take the necessary values in order to initialize the
	 * instance fields. 
	 */
	public TermImp(String term) {

		if(term.contains("x^")) { //Contains an exponent != 1

			if(term.startsWith("x")) this.setCoefficient(1);
			else if(term.startsWith("-x")) this.setCoefficient(-1);
			else this.setCoefficient(Double.valueOf(term.substring(0, term.indexOf("x"))));

			int exp = Integer.valueOf(term.substring(term.indexOf("^")+1,term.length()));
			if(exp < 0) throw new IllegalArgumentException("Polynomials cannot have negative exponents.");
			this.setExponent(exp);

		}else if(term.contains("x")) { //Contains an exponent == 1

			this.setExponent(1);
			if(term.startsWith("x")) this.setCoefficient(1);
			else if(term.startsWith("-x")) this.setCoefficient(-1);
			else {
				this.setCoefficient(Double.valueOf(term.substring(0, term.indexOf("x"))));
			}
		}else { // Does not contains an x
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
	
	/**
	 * This method returns the String representation of the term
	 * 
	 * Note: This helps the String representation of a Polynomial
	 */
	@Override
	public String toString() {
		if(this.getExponent() == 0) return String.format("%.2f", this.getCoefficient())+"";

		StringBuilder str = new StringBuilder();

		if(this.getExponent() == 1) str.append(String.format("%.2f", this.getCoefficient())+"x");
		else str.append(String.format("%.2f", this.getCoefficient())+"x^"+this.getExponent());

		return str.toString();
	}

}
