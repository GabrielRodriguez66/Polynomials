package edu.uprm.ece.icom4035.polynomial;

public class TermImp implements Term {
	private double coefficient;
	private int exponent;
	
	public TermImp(double coeff, int exp) {
		this.coefficient = coeff;
		this.exponent = exp;
	}

	@Override
	public double evaluate(double x) {
		return this.coefficient*Math.pow(x, this.exponent);
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

}
