package edu.iastate.cs228.hw4;

import java.util.HashMap;

/**
 * 
 * @author Christian Lisle
 * 
 *         This class represents an infix expression. It implements infix to
 *         postfix conversion using one stack, and evaluates the converted
 *         postfix expression.
 *
 */
public class InfixExpression extends Expression {
	private String infixExpression; // the infix expression to convert
	private boolean postfixReady = false; // postfix already generated if true
	private int rankTotal = 0; // Keeps track of the cumulative rank of the infix expression.

	private PureStack<Operator> operatorStack; // stack of operators

	/**
	 * Constructor stores the input infix string, and initializes the operator stack
	 * and the hash map.
	 * 
	 * @param st input infix string.
	 * @param varTbl hash map storing all variables in the infix expression and
	 *            their values.
	 */
	public InfixExpression(String st, HashMap<Character, Integer> varTbl) {
		super("", varTbl);
		infixExpression = st;
		operatorStack = new ArrayBasedStack<Operator>();
	}

	/**
	 * Constructor supplies a default hash map.
	 * 
	 * @param s
	 */
	public InfixExpression(String s) {
		super("");
		infixExpression = s;
		operatorStack = new ArrayBasedStack<Operator>();
	}

	/**
	 * Outputs the infix expression according to the format in the project
	 * description.
	 */
	@Override
	public String toString() {
		String r = removeExtraSpaces(infixExpression);
		r = r.replace("( ", "(");
		r = r.replace(" )", ")");
		return r;
	}

	/**
	 * @return equivalent postfix expression, or
	 * 
	 *         a null string if a call to postfix() inside the body (when
	 *         postfixReady == false) throws an exception.
	 */
	public String postfixString() {
		try {
			if (!postfixReady) postfix();
		}
		catch (ExpressionFormatException e) {
			return null;
		}

		return postfixExpression;
	}

	/**
	 * Resets the infix expression.
	 * 
	 * @param st
	 */
	public void resetInfix(String st) {
		infixExpression = st;
		postfixReady = false;
		rankTotal = 0;
	}
	
	/**
	 * Converts infix expression to an equivalent postfix string stored at
	 * postfixExpression. If postfixReady == false, the method scans the
	 * infixExpression, and does the following (for algorithm details refer to the
	 * relevant PowerPoint slides):
	 * 
	 * 1. Skips a whitespace character.
	 * 
	 * 2. Writes a scanned operand to postfixExpression.
	 * 
	 * 3. When an operator is scanned, generates an operator object. In case the
	 * operator is determined to be a unary minus, store the char '~' in the
	 * generated operator object.
	 * 
	 * 4. If the scanned operator has a higher input precedence than the stack
	 * precedence of the top operator on the operatorStack, push it onto the stack.
	 * 
	 * 5. Otherwise, first calls outputHigherOrEqual() before pushing the scanned
	 * operator onto the stack. No push if the scanned operator is ).
	 * 
	 * 6. Keeps track of the cumulative rank of the infix expression.
	 * 
	 * During the conversion, catches errors in the infixExpression by throwing
	 * ExpressionFormatException with one of the following messages:
	 * 
	 * -- "Operator expected" if the cumulative rank goes above 1; -- "Operand
	 * expected" if the rank goes below 0; -- "Missing '('" if scanning a ‘)’
	 * results in popping the stack empty with no '('; -- "Missing ')'" if a '(' is
	 * left unmatched on the stack at the end of the scan; -- "Invalid character" if
	 * a scanned char is neither a digit nor an operator;
	 * 
	 * If an error is not one of the above types, throw the exception with a message
	 * you define.
	 * 
	 * Sets postfixReady to true.
	 */
	public void postfix() throws ExpressionFormatException {
		String inf = removeAllSpaces(infixExpression);
		postfixExpression = "";
		for (int i = 0; i < inf.length(); i++) {
			if (!isOperator(inf.charAt(i)) && !isInt(inf.charAt(i) + "") && !isVariable(inf.charAt(i))) {
				throw new ExpressionFormatException("Invalid character");
			}
			if (isOperator(inf.charAt(i))) {
				Operator op = new Operator(inf.charAt(i));
				if (inf.charAt(i) == '-') {
					if (i == 0 || (i > 0 && inf.charAt(i - 1) != ')' && isOperator(inf.charAt(i - 1)))) {
						op = new Operator('~');
					}
				}
				if (!operatorStack.isEmpty()) {
					Operator s = operatorStack.peek();
					if (s.compareTo(op) < 0) {
						if (s.operator == '(' && op.operator == ')') operatorStack.pop();
						else operatorStack.push(op);
					}
					else {
						outputHigherOrEqual(op);
						if (op.operator != ')') {
							operatorStack.push(op);
						}
					}
				}
				else operatorStack.push(op);
				switch (op.operator) {
				case '+':
				case '-':
				case '*':
				case '/':
				case '%':
				case '^':
					rankTotal -= 1;
				}
			}
			else { // Adds operand (variable or int) to postfix expression
				rankTotal += 1;
				postfixExpression = postfixExpression + inf.charAt(i);
				int j = i + 1;
				while (j < inf.length() - 1 && isInt(inf.substring(i, j + 1))) {
					postfixExpression = postfixExpression + inf.charAt(j++);
					i++;
				}
				postfixExpression = postfixExpression + ' ';
			}
			if (rankTotal > 1) throw new ExpressionFormatException("Operator expected");
			if (rankTotal < 0) throw new ExpressionFormatException("Operand expected");
		}
		while (!operatorStack.isEmpty()) { // Add any remaining operators in stack to expression
			Operator op = operatorStack.pop();
			if (op.operator == '(') throw new ExpressionFormatException("Missing ')'");
			postfixExpression = postfixExpression + op.operator + ' ';
		}
		if (rankTotal != 1) throw new ExpressionFormatException("Incorrect number of operands and/or operators");
		postfixExpression = removeExtraSpaces(postfixExpression);
		postfixReady = true;
	}

	/**
	 * This function first calls postfix() to convert infixExpression into
	 * postfixExpression. Then it creates a PostfixExpression object and calls its
	 * evaluate() method (which may throw an exception). It also passes any
	 * exception thrown by the evaluate() method of the PostfixExpression object
	 * upward the chain.
	 * 
	 * @return value of the infix expression
	 * @throws ExpressionFormatException, UnassignedVariableException
	 */
	public int evaluate() throws ExpressionFormatException, UnassignedVariableException {
		if (!postfixReady) postfix();
		PostfixExpression p = new PostfixExpression(postfixExpression, varTable);
		return p.evaluate();
	}

	/**
	 * Pops the operator stack and output as long as the operator on the top of the
	 * stack has a stack precedence greater than or equal to the input precedence of
	 * the current operator op. Writes the popped operators to the string
	 * postfixExpression.
	 * 
	 * If op is a ')', and the top of the stack is a '(', also pops '(' from the
	 * stack but does not write it to postfixExpression.
	 * 
	 * @param op current operator
	 * @throws ExpressionFormatException if scanning a ')' results in popping the
	 *             stack empty with no '(' (thrown up the chain to the postfix()
	 *             method)
	 */
	private void outputHigherOrEqual(Operator op) throws ExpressionFormatException {
		Operator p = operatorStack.peek();
		if (op.operator != ')') {
			while (p.compareTo(op) >= 0) {
				p = operatorStack.pop();
				postfixExpression = postfixExpression + p.operator + ' ';
				if (!operatorStack.isEmpty()) p = operatorStack.peek();
				else break; // Breaks out of loop if stack is empty (otherwise peeks at top)
			}
		}
		else {
			boolean removedParenthesis = false;
			while (p.operator != '(' && !operatorStack.isEmpty()) {
				p = operatorStack.pop();
				postfixExpression = postfixExpression + p.operator + ' ';
				if (!operatorStack.isEmpty()) p = operatorStack.peek();
				else break; // Breaks out of loop if stack is empty (otherwise peeks at top)
			}
			if (p.operator == '(') {
				operatorStack.pop();
				removedParenthesis = true;
			}
			if (operatorStack.isEmpty() && !removedParenthesis) throw new ExpressionFormatException("Missing '('");
		}
	}
}
