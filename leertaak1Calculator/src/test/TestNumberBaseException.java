package test;

import multiformat.Calculator;
import multiformat.NumberBaseException;
import multiformat.FormatException;
import multiformat.Rational;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;



public class TestNumberBaseException {

	
	@Rule public ExpectedException thrown= ExpectedException.none();
	
	@Test
	public void TestNoException() throws FormatException, NumberBaseException{
		Calculator calc = new Calculator();
		thrown.expect(NumberBaseException.class);
			calc.addOperand("1.2");
	}
	
	@Test
	public void TestException() throws FormatException, NumberBaseException{
		Calculator calc = new Calculator();
		thrown.expect(NumberBaseException.class);
		calc.addOperand("1A");
	}
}
