package edu.iastate.cs228.hw4;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

/**
 * 
 * @author Christian Lisle
 * 
 *         This class evaluates input infix and postfix expressions.
 *
 */
public class InfixPostfix {

	/**
	 * Repeatedly evaluates input infix and postfix expressions. See the project
	 * description for the input description. It constructs a HashMap object for
	 * each expression and passes it to the created InfixExpression or
	 * PostfixExpression object.
	 * 
	 * @param args
	 * @throws FileNotFoundException
	 **/
	public static void main(String[] args) throws FileNotFoundException {
		Scanner input = new Scanner(System.in);
		int choice = 0;
		int trial = 1;
		System.out.println("Evaluation of Infix and Postfix Expressions\nkeys: 1 (standard input) 2 (file input) 3 (exit)");
		System.out.println("(Enter \"I\" before an infix expression, \"P\" before a postfix expression)");

		while (choice == 1 || choice == 2 || (choice == 0 && trial == 1)) {
			System.out.print("\nTrial " + trial + ": ");
			choice = input.nextInt();
			if (choice == 1) {
				System.out.print("Expression: ");
				String type = input.next();
				if (type.equals("I")) { // Infix Expression
					String expr = input.nextLine();
					InfixExpression infixExpr = new InfixExpression(expr);
					System.out.println("Infix form: " + infixExpr.toString());
					System.out.println("Postfix form: " + infixExpr.postfixString());
					HashMap<Character, Integer> var = getVariables(expr, input);
					if (var != null) infixExpr = new InfixExpression(expr, var);
					try {
						System.out.println("Expression value: " + infixExpr.evaluate());
					}
					catch (ExpressionFormatException | UnassignedVariableException e) {
						System.out.println(e.getMessage());
					}
				}
				else if (type.equals("P")) { // Postfix Expression
					String expr = input.nextLine();
					PostfixExpression postfixExpr = new PostfixExpression(expr);
					System.out.println("Postfix form: " + postfixExpr.toString());
					HashMap<Character, Integer> var = getVariables(expr, input);
					if (var != null) postfixExpr = new PostfixExpression(expr, var);
					try	{
						System.out.println("Expression value: " + postfixExpr.evaluate());
					}
					catch (ExpressionFormatException | UnassignedVariableException e) {
						System.out.println(e.getMessage());
					}
				}
			}
			else if (choice == 2) {
				System.out.print("Input from a file\nEnter file name: ");
				String fileName = input.next();
				Scanner fileInput = new Scanner(new File(fileName));
				while (fileInput.hasNextLine()) {
					String type = fileInput.next();
					if (type.charAt(0) == 'I') { // Infix Expression
						String expr = fileInput.nextLine();
						InfixExpression infixExpr = new InfixExpression(expr);
						System.out.println("\nInfix form: " + infixExpr.toString());
						System.out.println("Postfix form: " + infixExpr.postfixString());
						HashMap<Character, Integer> var = getVariablesForFile(expr, fileInput);
						if (var != null) infixExpr = new InfixExpression(expr, var);
						try	{
							System.out.println("Expression value: " + infixExpr.evaluate());
						}
						catch (ExpressionFormatException | UnassignedVariableException e) {
							System.out.println(e.getMessage());
						}
					}
					else if (type.charAt(0) == 'P') { // Postfix Expression
						String expr = fileInput.nextLine();
						PostfixExpression postfixExpr = new PostfixExpression(expr);
						System.out.println("\nPostfix form: " + postfixExpr.toString());
						HashMap<Character, Integer> var = getVariablesForFile(expr, fileInput);
						if (var != null) postfixExpr = new PostfixExpression(expr, var);
						try	{
							System.out.println("Expression value: " + postfixExpr.evaluate());
						}
						catch (ExpressionFormatException | UnassignedVariableException e) {
							System.out.println(e.getMessage());
						}
					}
				}
				fileInput.close();
			}
			else break; // break out of loop if choice is not 1 or 2. Important for first iteration

			trial++;
		}
		input.close();
	}

	/**
	 * Finds the number of variables that need to be assigned with a value. This is
	 * a helper method that I (Christian Lisle) created for my own implementation.
	 * 
	 * @param expression to be scanned for variables
	 * @return number of variables in expression
	 */
	private static int variableCount(String expression) {
		int count = 0;
		for (int i = 0; i < expression.length(); i++) {
			if (Expression.isVariable(expression.charAt(i))) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Finds all variables within expression that need assigned values and prompts
	 * user for each value. This is a helper method that I (Christian Lisle) created
	 * for my own implementation.
	 * 
	 * @param expression
	 * @param input scanner for user input
	 * @return HashMap of variables and their values if expression contains
	 *         variables. Null otherwise.
	 */
	private static HashMap<Character, Integer> getVariables(String expression, Scanner input) {
		if (variableCount(expression) > 0) {
			HashMap<Character, Integer> varMap = new HashMap<Character, Integer>();
			System.out.println("where");
			for (int i = 0; i < expression.length(); i++) {
				if (Expression.isVariable(expression.charAt(i)) && !varMap.containsKey(expression.charAt(i))) {
					System.out.print(expression.charAt(i) + " = ");
					int val = input.nextInt();
					varMap.put(expression.charAt(i), val);
				}
			}
			return varMap;
		}
		return null;
	}

	/**
	 * Finds all variables within expression that need assigned values and searches
	 * for them within file. This is a helper method that I (Christian Lisle)
	 * created for my own implementation.
	 * 
	 * @param expression
	 * @param input scanner for parsing file
	 * @return HashMap of variables and their values if expression contains
	 *         variables. Null otherwise.
	 */
	private static HashMap<Character, Integer> getVariablesForFile(String expression, Scanner input) {
		if (variableCount(expression) > 0) {
			HashMap<Character, Integer> varMap = new HashMap<Character, Integer>();
			int varsFound = 0;
			System.out.println("where");
			while (input.hasNextLine() && varsFound < variableCount(expression)) {
				String line = input.nextLine();
				System.out.println(Expression.removeExtraSpaces(line)); // Prints line with variable value (i.e. "a = 1")
				Scanner lineScan = new Scanner(line);
				String var = lineScan.next();
				lineScan.next(); // Skip '=' 
				varMap.put(var.charAt(0), lineScan.nextInt());
				lineScan.close();
				varsFound++;
			}
			return varMap;
		}
		return null;
	}
}
