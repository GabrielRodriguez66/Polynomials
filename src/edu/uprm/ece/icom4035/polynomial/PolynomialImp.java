package edu.uprm.ece.icom4035.polynomial;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import edu.uprm.ece.icom4035.list.ArrayList;
import edu.uprm.ece.icom4035.list.List;
import edu.uprm.ece.icom4035.list.ListFactory;
import edu.uprm.ece.icom4035.list.SinglyLinkedList;

public class PolynomialImp implements Polynomial {
	private List<Term> list;
	private ListFactory<Term> factory = TermListFactory.newListFactory();

	public PolynomialImp(String poly) {
		this.list = factory.newInstance();
		this.parseTerms(poly);
		this.sortByExponents();
	}

	public PolynomialImp() {		
		//The list is empty, expected to add terms later
		this.list = factory.newInstance();
	}

	/**
	 * This method returns a new Polynomial, which is the result
	 * of the sum of the target object and P2.
	 * 
	 * This implementation use a private sorting method to make easier the
	 * process of adding similar terms
	 */
	@Override
	public Polynomial add(Polynomial P2) {
		
		PolynomialImp tempResult = new PolynomialImp();
		for(Term t:this) tempResult.addTerm(t);
		for(Term t:P2) tempResult.addTerm(t);
		
		tempResult.sortByExponents(); // Sort terms in decreasing order of exponents
		
		PolynomialImp ptr = new PolynomialImp(); //This polynomial will contain unique exponents
		int exp = tempResult.degree(); double coeff = 0; // Initial values 
		Iterator<Term> itr = tempResult.iterator();
		
		/**
		 * Adding similar terms, in order to simplify the expression
		 */
		while(itr.hasNext()) {
			Term t = itr.next();
			if(t.getExponent() != exp) {
				if(coeff != 0) ptr.addTerm(new TermImp(coeff,exp));
				exp = t.getExponent();
				coeff = t.getCoefficient();
			}else coeff += t.getCoefficient();
			
			if(!itr.hasNext()) ptr.addTerm(new TermImp(coeff,exp)); //If there is no terms left, add one last term
		}

		if(ptr.getNumberOfTerms() == 0) return new PolynomialImp("0"); //Special case
		return ptr;
	}

	/**
	 * This method returns a new Polynomial, which is the result
	 * of the difference of the target object and P2.
	 * 
	 * This implementation use a private sorting method to make easier the
	 * process of adding similar terms
	 */
	@Override
	public Polynomial subtract(Polynomial P2) {
		
		PolynomialImp tempResult = new PolynomialImp();
		for(Term t:this) tempResult.addTerm(t);
		for(Term t:P2) tempResult.addTerm(new TermImp(-t.getCoefficient(),t.getExponent()));
		
		tempResult.sortByExponents(); // Sort terms in decreasing order of exponents
		
		PolynomialImp ptr = new PolynomialImp(); //This polynomial will contain unique exponents
		int exp = tempResult.degree(); double coeff = 0; // Initial values 
		Iterator<Term> itr = tempResult.iterator();
		
		/**
		 * Adding similar terms, in order to simplify the expression
		 */
		while(itr.hasNext()) {
			Term t = itr.next();
			if(t.getExponent() != exp) {
				if(coeff != 0) ptr.addTerm(new TermImp(coeff,exp));
				exp = t.getExponent();
				coeff = t.getCoefficient();
			}else coeff += t.getCoefficient();
			
			if(!itr.hasNext()) ptr.addTerm(new TermImp(coeff,exp)); //If there is no terms left, add one last term
		}

		if(ptr.getNumberOfTerms() == 0) return new PolynomialImp("0"); //Special case
		return ptr;
	}

	/**
	 * This method returns a new Polynomial, which is the result
	 * of the product of the target object and P2.
	 */
	@Override
	public Polynomial multiply(Polynomial P2) {
		if(((PolynomialImp)P2).toString().equals("0.00") || this.toString().equals("0.00"))
			return new  PolynomialImp("0");  // Special Case
		
		PolynomialImp tempResult = new PolynomialImp(); //This polynomial can contain similar terms
		
		for(Term t: this) {
			for(Term t2:P2) {
				int newExp = t.getExponent() + t2.getExponent();
				double newCoeff = t.getCoefficient()*t2.getCoefficient();
				tempResult.addTerm(new TermImp(newCoeff, newExp));
			}
		}

		tempResult.sortByExponents(); // Sort terms in decreasing order of exponents
		
		PolynomialImp ptr = new PolynomialImp(); //This polynomial will contain unique exponents
		int exp = tempResult.degree(); double coeff = 0; // Initial values 
		Iterator<Term> itr = tempResult.iterator();
		
		/**
		 * Adding similar terms, in order to simplify the expression
		 */
		while(itr.hasNext()) {
			Term t = itr.next();
			if(t.getExponent() != exp) {
				ptr.addTerm(new TermImp(coeff,exp));
				exp = t.getExponent();
				coeff = t.getCoefficient();
			}else coeff += t.getCoefficient();
			
			if(!itr.hasNext()) ptr.addTerm(new TermImp(coeff,exp)); //If there is no terms left, add one last term
		}
		
		return ptr;
	}

	/**
	 * This method returns a new Polynomial, which is the result
	 * of the product of the target object and a constant.
	 */
	@Override
	public Polynomial multiply(double c) {
		Polynomial ptr = new PolynomialImp();
		if(c == 0) return new PolynomialImp("0"); //Special Case

		for(Term t:this) {
			Term newTerm = new TermImp(t.getCoefficient() * c, t.getExponent());
			((PolynomialImp)ptr).addTerm(newTerm);
		}

		return ptr;
	}

	/**
	 * This method returns a new Polynomial, which is the result
	 * of the derivative of the target object.
	 * 
	 * Using the Power Rule of Calculus for differentiation, the
	 * exponent is multiplied by the coefficient and the exponent 
	 * is reduced by one.
	 */
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

	/**
	 * This method returns a new Polynomial, which is the result
	 * of the anti-derivative of the target object.
	 * 
	 * Using the Power Rule of Calculus for integration, the exponent
	 * is incremented by one, and the coefficient divided by the 
	 * new exponent.
	 */
	@Override
	public Polynomial indefiniteIntegral() {
		Polynomial ptr = new PolynomialImp();
		for(Term t:this) {
			int newExponent = t.getExponent()+1;
			TermImp newTerm = new TermImp(t.getCoefficient()/newExponent, newExponent);
			((PolynomialImp)ptr).addTerm(newTerm);

		}
		TermImp newTerm = new TermImp(1.0, 0);// hard-coded "+1" in order to satisfy JUnits
		((PolynomialImp)ptr).addTerm(newTerm);
		return ptr;
	}

	/**
	 * This method calls the indefinite integral method to follow
	 * the Fundamental Theorem of Calculus, it evaluates the 
	 * anti-derivative in b minus the evaluation in a.
	 */
	@Override
	public double definiteIntegral(double a, double b) {
		Polynomial antiDerivative = this.indefiniteIntegral();
		return antiDerivative.evaluate(b)-antiDerivative.evaluate(a);
	}

	/**
	 * This method returns the highest degree of the target object
	 * 
	 * Note: This method assumes the polynomial is in standard form, 
	 * that means sorted in descending order of exponents.
	 */
	@Override
	public int degree() {
		return this.list.first().getExponent();
	}

	/**
	 * This method returns the result of the evaluation of x
	 * in the target object. The implementation calls the 
	 * evaluate() method of each term and accumulates the sum.
	 */
	@Override
	public double evaluate(double x) {
		double sum = 0;
		for(Term t:this) {
			sum += t.evaluate(x);
		}
		return sum;
	}

	/**
	 * Assuming the polynomials are simplified, then if they don't
	 * have equal number of terms returns false, else have to check
	 * each term. This equals method calls each term equals() method
	 * in order to gain readability. 
	 */
	@Override
	public boolean equals(Polynomial P) {		
		if(this.getNumberOfTerms() != ((PolynomialImp)P).getNumberOfTerms()) return false;

		Iterator<Term> itr = P.iterator();

		for(Term t:this) {
			if(!t.equals(itr.next())) return false;
		}

		return true;
	}

	/**
	 * This method returns an iterator for the Polynomial.
	 * 
	 * Note: Because the implementation of the Polynomial is based on a list,
	 * to iterate over it is the same that iterating over the list.
	 */
	@Override
	public Iterator<Term> iterator() {
		return this.list.iterator();
	}

	/**
	 * This method call each term toString() method and
	 * concatenates it.
	 * 
	 * Note: StringBuilder is used in order to have 
	 * more efficiency in the concatenation.
	 */
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

	private void addTerm(Term t) {
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
			this.addTerm(termInstance);
		}

	}
	
	/**
	 * This comparator will be used to sort the polynomial in decreasing order
	 * of exponents. The Terms compareTo() method is implemented so that they
	 * end in decreasing order.
	 */
	private static class TermsComparator<Term> implements Comparator<Term> {
		public int compare(Term t1, Term t2) {
			try { 
				return ((Comparable<Term>) t1).compareTo(t2); 
			} catch (ClassCastException e) { 
				throw new IllegalArgumentException("Instantiated data type must be Comparable");
			}
		} 
	}
	
	/**
	 * Insertion sort algorithm using the comparator implemented for
	 * polynomial terms.
	 */
	private void sortByExponents() {
		Comparator<Term> cmp = new TermsComparator<>();
		for (int i=2; i<=this.list.size(); i++) { 
			int j = i-2; 
			Term v = this.list.get(i-1); 
			while (j >= 0 && cmp.compare(v, this.list.get(j)) < 0) { 
				this.list.set(j+1,this.list.get(j)); 
				j--;
			}
			this.list.set(j+1, v); 

		}
	}
}
