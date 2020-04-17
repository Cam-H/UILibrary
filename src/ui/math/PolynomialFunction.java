package ui.math;

import java.util.ArrayList;
import java.util.List;

public class PolynomialFunction extends Function {

	protected List<Double> coefficients;
	
	public PolynomialFunction() {
		this(0);
	}
	
	public PolynomialFunction(double A) {
		coefficients = new ArrayList<Double>();
		
		coefficients.add(A);
	}
	
	public PolynomialFunction(double A, double B) {
		coefficients = new ArrayList<Double>();
		
		coefficients.add(A);
		coefficients.add(B);
	}
	
	public PolynomialFunction(double A, double B, double C) {
		coefficients = new ArrayList<Double>();
		
		coefficients.add(A);
		coefficients.add(B);
		coefficients.add(C);
	}
	
	public PolynomialFunction(List<Double> coefficients) {
		this.coefficients = coefficients;
	}
	
	public double getValueAt(double t) {
		return getValueAt(t, 0);
	}
	
	public double getValueAt(double t, int i) {
		return coefficients.get(i) * Math.pow(t, coefficients.size() - i - 1) + (i + 1 < coefficients.size() ? getValueAt(t, i + 1) : 0);
	}
	
	@Override
	public Point get(double t) {
		return new Point(t, getValueAt(t));
	}

}
