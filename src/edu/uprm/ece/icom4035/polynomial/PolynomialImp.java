package edu.uprm.ece.icom4035.polynomial;

import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import edu.uprm.ece.icom4035.list.ArrayList;
import edu.uprm.ece.icom4035.list.List;
import edu.uprm.ece.icom4035.list.ListFactory;

public class PolynomialImp implements Polynomial {
	private List<Term> list;
	private ListFactory<Term> factory = TermListFactory.newListFactory();

	public PolynomialImp(String poly) {
		this.list = factory.newInstance();
		this.parseTerms(poly);
	}

	public PolynomialImp() {		
		this.list = factory.newInstance();
	}

	@Override
	public Polynomial add(Polynomial P2) {
		HashMap<Integer,Double> resultExpCoeffMap = new HashMap<Integer,Double>();
		
		for(Term t: this) {
			resultExpCoeffMap.put(t.getExponent(), t.getCoefficient());
		}
		
		for(Term t:P2) {
			if(!resultExpCoeffMap.containsKey(t.getExponent())) resultExpCoeffMap.put(t.getExponent(), t.getCoefficient());
			else resultExpCoeffMap.put(t.getExponent(), t.getCoefficient()+resultExpCoeffMap.get(t.getExponent()));
		}
		
		PolynomialImp ptr = new PolynomialImp();
		Integer[]  keys = resultExpCoeffMap.keySet().toArray(new Integer[0]);		
		for(int i = 0; i<resultExpCoeffMap.size();i++) {
			double newCoeff = resultExpCoeffMap.get(keys[keys.length-1-i]);
			if(newCoeff != 0) {
				int newExp = keys[keys.length-1-i];
				TermImp newTerm = new TermImp(newCoeff,newExp);
				ptr.addTerm(newTerm);
			}
		}
		
		return ptr;
	}

	@Override
	public Polynomial subtract(Polynomial P2) {
	HashMap<Integer,Double> resultExpCoeffMap = new HashMap<Integer,Double>();
		
		for(Term t: this) {
			resultExpCoeffMap.put(t.getExponent(), t.getCoefficient());
		}
		
		for(Term t:P2) {
			if(!resultExpCoeffMap.containsKey(t.getExponent())) resultExpCoeffMap.put(t.getExponent(), -t.getCoefficient());
			else resultExpCoeffMap.put(t.getExponent(), resultExpCoeffMap.get(t.getExponent())-t.getCoefficient());
		}
		
		Integer[]  keys = resultExpCoeffMap.keySet().toArray(new Integer[0]);
		PolynomialImp ptr = new PolynomialImp();
		for(int i = 0; i<resultExpCoeffMap.size();i++) {
			double newCoeff = resultExpCoeffMap.get(keys[keys.length-1-i]);
			if(newCoeff != 0) {
				int newExp = keys[keys.length-1-i];
				TermImp newTerm = new TermImp(newCoeff,newExp);
				ptr.addTerm(newTerm);
			}
		}
		
		if(ptr.getNumberOfTerms() == 0) return new  PolynomialImp("0");
		return ptr;
	}

	@Override
	public Polynomial multiply(Polynomial P2) {// TODO
		HashMap<Integer,Double> resultExpCoeffMap = new HashMap<Integer,Double>();

		for(Term t: this) {
			double tCoeff = t.getCoefficient();
			for(Term t2:P2) {
				int newExp = t.getExponent() + t2.getExponent();
				if(!resultExpCoeffMap.containsKey(newExp)) resultExpCoeffMap.put(newExp, t2.getCoefficient()*tCoeff);
				else resultExpCoeffMap.put(newExp, resultExpCoeffMap.get(newExp)+t2.getCoefficient()*tCoeff);
			}
		}
		
		PolynomialImp ptr = new PolynomialImp();
		Integer[]  keys = resultExpCoeffMap.keySet().toArray(new Integer[0]);		
		for(int i = 0; i<resultExpCoeffMap.size();i++) {
			double newCoeff = resultExpCoeffMap.get(keys[keys.length-1-i]);
			if(newCoeff != 0) {
				int newExp = keys[keys.length-1-i];
				TermImp newTerm = new TermImp(newCoeff,newExp);
				ptr.addTerm(newTerm);
			}
		}
		
		if(ptr.getNumberOfTerms() == 0) return new  PolynomialImp("0");
		return ptr;
	}

	@Override
	public Polynomial multiply(double c) {
		Polynomial ptr = new PolynomialImp();
		if(c == 0) return new PolynomialImp("0");

		for(Term t:this) {
			Term newTerm = new TermImp(t.getCoefficient() * c, t.getExponent());
			((PolynomialImp)ptr).addTerm(newTerm);
		}

		return ptr;
	}

	@Override
	public Polynomial derivative() {
		Polynomial ptr = new PolynomialImp();
		for(Term t:this) {
			if(t.getExponent() != 0) {
				TermImp newTerm = new TermImp(t.getCoefficient() * t.getExponent(), t.getExponent()-1);
				((PolynomialImp)ptr).addTerm(newTerm);
			}
		}
		return ptr;
	}

	@Override
	public Polynomial indefiniteIntegral() {
		Polynomial ptr = new PolynomialImp();
		for(Term t:this) {
			int newExponent = t.getExponent()+1;
			TermImp newTerm = new TermImp(t.getCoefficient()/newExponent, newExponent);
			((PolynomialImp)ptr).addTerm(newTerm);

		}
		TermImp newTerm = new TermImp(1.0, 0);// temporal, test bug
		((PolynomialImp)ptr).addTerm(newTerm);
		return ptr;
	}

	@Override
	public double definiteIntegral(double a, double b) {
		Polynomial antiDerivative = this.indefiniteIntegral();
		return antiDerivative.evaluate(b)-antiDerivative.evaluate(a);
	}

	@Override
	public int degree() {
		return this.list.first().getExponent();
	}

	@Override
	public double evaluate(double x) {
		double sum = 0;
		for(Term t:this) {
			sum += t.evaluate(x);
		}
		return sum;
	}

	@Override
	public boolean equals(Polynomial P) {		
		if(this.getNumberOfTerms() != ((PolynomialImp)P).getNumberOfTerms()) return false;

		Iterator<Term> itr = P.iterator();

		for(Term t:this) {
			if(!t.equals(itr.next())) return false;
		}

		return true;
	}

	@Override
	public Iterator<Term> iterator() {
		return this.list.iterator();
	}

	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		boolean firstTerm = true;
		for(Term t:this) {
			if(!firstTerm) {
				str.append("+");
			}
			firstTerm = false;
			str.append(((TermImp)t).toString());
		}
		return str.toString();
	}

	public int getNumberOfTerms() {
		return this.list.size();
	}

	public void addTerm(Term t) {
		this.list.add(t);
	}

	/**
	 * This private helper method parse polynomial terms in 
	 * String form to TermpImp instances and add each one 
	 * to the internal List. 
	 * 
	 * Note: The TermImp constructor does the job of accessing
	 * the String and take the necessary values
	 * 
	 * @param polynomialString
	 */
	private void parseTerms(String polynomialString) {

		StringTokenizer strTkn = new StringTokenizer(polynomialString,"+");

		while(strTkn.hasMoreTokens()) {
			String term = strTkn.nextToken();
			Term termInstance = new TermImp(term);
			list.add(termInstance);
		}

	}

}
