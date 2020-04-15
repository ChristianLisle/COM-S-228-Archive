package edu.iastate.cs228.hw4;

import java.util.HashMap;

/**
 * 
 * @author Christian Lisle
 * 
 *         This class represents an abstract Expression.
 *
 */
public abstract class Expression {
	protected String postfixExpression;
	protected HashMap<Character, Integer> varTable; // hash map to store variables

	/**
	 * Initialization with a provided hash map.
	 * 
	 * @param varTbl
	 */
	protected Expression(String st, HashMap<Character, Integer> varTbl) {
		postfixExpression = st;
		setVarTable(new HashMap<Character, Integer>());
		varTable.putAll(varTbl);
	}

	/**
	 * Initialization with a default hash map.
	 * 
	 * @param st
	 */
	protected Expression(String st) {
		postfixExpression = st;
		setVarTable(new HashMap<Character, Integer>());
	}

	/**
	 * Setter for instance variable varTable.
	 * 
	 * @param varTbl
	 */
	public void setVarTable(HashMap<Character, Integer> varTbl) {
		varTable = varTbl;
	}

	/**
	 * Evaluates the infix or postfix expression.
	 * 
	 * @return value of the expression
	 * @throws ExpressionFormatException, UnassignedVariableException
	 */
	public abstract int evaluate() throws ExpressionFormatException, UnassignedVariableException;

	// --------------------------------------------------------
	// Helper methods for InfixExpression and PostfixExpression
	// --------------------------------------------------------

	/**
	 * Checks if a string represents an integer. You may call the static method
	 * Integer.parseInt().
	 * 
	 * @param s
	 * @return
	 */
	protected static boolean isInt(String s) {
		try {
			Integer.parseInt(s);
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if a char represents an operator, i.e., one of '~', '+', '-', '*',
	 * '/', '%', '^', '(', ')'.
	 * 
	 * @param c
	 * @return
	 */
	protected static boolean isOperator(char c) {
		switch (c) {
		case '~':
		case '+':
		case '-':
		case '*':
		case '/':
		case '%':
		case '^':
		case '(':
		case ')':
			return true;
		default:
			return false;
		}
	}

	/**
	 * Checks if a char is a variable, i.e., a lower case English letter.
	 * 
	 * @param c
	 * @return
	 */
	protected static boolean isVariable(char c) {
		return Character.isLowerCase(c);
	}

	/**
	 * Removes extra blank spaces in a string.
	 * 
	 * @param s
	 * @return String without extra blank spaces
	 */
	protected static String removeExtraSpaces(String s) {
		String r = s;
		r = r.replace('\t', ' ');
		r = r.replaceAll("\\s+", " ");
		return r.trim();
	}

	/**
	 * Removes all blank spaces in a string. This is a helper method that I
	 * (Christian Lisle) created for my own implementation.
	 * 
	 * @param s
	 * @return String with no blank spaces
	 */
	protected static String removeAllSpaces(String s) {
		String r = s;
		for (int i = 0; i < r.length(); i++) {
			if (r.charAt(i) == ' ' || r.charAt(i) == '	') {
				if (i < r.length() - 1) {
					r = r.substring(0, i) + r.substring(i + 1);
				}
				else r = r.substring(0, i);
				i--; // Decrement to shift index
			}
		}
		return r;
	}
}
