package edu.iastate.cs228.hw4;

import java.util.HashMap;
import java.util.NoSuchElementException;

/**
 * 
 * @author Christian Lisle
 * 
 *         This class evaluates a postfix expression using one stack.
 *
 */
public class PostfixExpression extends Expression {
	private int leftOperand; // left operand for the current evaluation step
	private int rightOperand; // right operand (or the only operand in the case of
								// a unary minus) for the current evaluation step

	private PureStack<Integer> operandStack; // stack of operands

	/**
	 * Constructor stores the input postfix string and initializes the operand
	 * stack.
	 * 
	 * @param st input postfix string.
	 * @param varTbl hash map that stores variables from the postfix string and
	 *            their values.
	 */
	public PostfixExpression(String st, HashMap<Character, Integer> varTbl) {
		super(st, varTbl);
		operandStack = new ArrayBasedStack<Integer>();
	}

	/**
	 * Constructor supplies a default hash map.
	 * 
	 * @param s input postfix string
	 */
	public PostfixExpression(String s) {
		super(s);
		operandStack = new ArrayBasedStack<Integer>();
	}

	/**
	 * Outputs the postfix expression according to the format in the project
	 * description.
	 */
	@Override
	public String toString() {
		String r = removeExtraSpaces(postfixExpression);
		String output = "";
		for (int i = 0; i < r.length(); i++) {
			if (i < r.length() - 1 && !isInt(r.charAt(i + 1) + ""))	{
				output = output + r.charAt(i) + ' ';
			}
			else output = output + r.charAt(i);
		}
		return removeExtraSpaces(output);
	}

	/**
	 * Resets the postfix expression.
	 * 
	 * @param st
	 */
	public void resetPostfix(String st) {
		postfixExpression = st;
	}

	/**
	 * Scan the postfixExpression and carry out the following:
	 * 
	 * 1. Whenever an integer is encountered, push it onto operandStack. 2. Whenever
	 * a binary (unary) operator is encountered, invoke it on the two (one) elements
	 * popped from operandStack, and push the result back onto the stack. 3. On
	 * encountering a character that is not a digit, an operator, or a blank space,
	 * stop the evaluation.
	 * 
	 * @return value of the postfix expression
	 * @throws ExpressionFormatException with one of the messages below:
	 * 
	 *             -- "Invalid character" if encountering a character that is not a
	 *             digit, an operator or a whitespace (blank, tab); -- "Too many
	 *             operands" if operandStack is non-empty at the end of evaluation;
	 *             -- "Too many operators" if getOperands() throws
	 *             NoSuchElementException; -- "Divide by zero" if division or modulo
	 *             is the current operation and rightOperand == 0; -- "0^0" if the
	 *             current operation is "^" and leftOperand == 0 and rightOperand ==
	 *             0; -- self-defined message if the error is not one of the above.
	 * 
	 * @throws UnassignedVariableException if the operand as a variable does not
	 *             have a value stored in the hash map. In this case, the exception
	 *             is thrown with the message
	 * 
	 *             -- "Variable <name> was not assigned a value", where <name> is
	 *             the name of the variable.
	 * 
	 */
	public int evaluate() throws ExpressionFormatException, UnassignedVariableException {
		String postf = removeExtraSpaces(postfixExpression);
		for (int i = 0; i < postf.length(); i++) {
			if (isInt(postf.charAt(i) + "") || isVariable(postf.charAt(i))) {
				if (isInt(postf.charAt(i) + "")) {
					int t = Character.getNumericValue(postf.charAt(i));
					int j = i + 1;
					while (j < postf.length() - 1 && isInt(postf.substring(i, j + 1))) {
						t = Integer.parseInt(postf.substring(i, ++j));
					}
					operandStack.push(t);
					if (j > i + 1) {
						i = j - 1;
					}
				}
				else if (isVariable(postf.charAt(i))) {
					char var = postf.charAt(i);
					if (varTable.get(var) == null) throw new UnassignedVariableException("Variable " + var + " was not assigned a value");
					operandStack.push(varTable.get(var));
				}
			}
			else if (isOperator(postf.charAt(i))) {
				try {
					getOperands(postf.charAt(i));
				}
				catch (NoSuchElementException e) {
					throw new ExpressionFormatException("Too many operators");
				}
				if (rightOperand == 0 && (postf.charAt(i) == '/' || postf.charAt(i) == '%')) throw new ExpressionFormatException("Divide by zero");
				if (leftOperand == 0 && rightOperand == 0 && postf.charAt(i) == '^') throw new ExpressionFormatException("0^0");
				operandStack.push(compute(postf.charAt(i)));
			}
			else if (postf.charAt(i) != ' ' && postf.charAt(i) != '	') {
				throw new ExpressionFormatException("Invalid Character");
			}
		}
		int r = operandStack.pop();
		if (!operandStack.isEmpty()) throw new ExpressionFormatException("Too many operands");
		return r;
	}

	/**
	 * For unary operator, pops the right operand from operandStack, and assign it
	 * to rightOperand. The stack must have at least one entry. Otherwise, throws
	 * NoSuchElementException. For binary operator, pops the right and left operands
	 * from operandStack, and assign them to rightOperand and leftOperand,
	 * respectively. The stack must have at least two entries. Otherwise, throws
	 * NoSuchElementException.
	 * 
	 * @param op char operator for checking if it is binary or unary operator.
	 */
	private void getOperands(char op) throws NoSuchElementException {
		switch (op) {
		case '~':
			if (operandStack.isEmpty()) throw new NoSuchElementException();
			rightOperand = operandStack.pop();
			break;
		case '+':
		case '-':
		case '*':
		case '/':
		case '%':
		case '^':
			if (operandStack.size() < 2) throw new NoSuchElementException();
			rightOperand = operandStack.pop();
			leftOperand = operandStack.pop();
			break;
		}
	}

	/**
	 * Computes "leftOperand op rightOprand" or "op rightOprand" if a unary
	 * operator.
	 * 
	 * @param op operator that acts on leftOperand and rightOperand.
	 * @return result
	 */
	private int compute(char op) {
		switch (op) {
		case '~':
			return 0 - rightOperand;
		case '+':
			return leftOperand + rightOperand;
		case '-':
			return leftOperand - rightOperand;
		case '*':
			return leftOperand * rightOperand;
		case '/':
			return leftOperand / rightOperand;
		case '%':
			return leftOperand % rightOperand;
		case '^':
			return (int) Math.pow(leftOperand, rightOperand);
		}
		return 0;
	}
}
