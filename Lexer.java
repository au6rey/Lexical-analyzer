//Aubrey Oyoolo
//Bruce Quach

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

enum Token {
	IF, FOR, WHILE, FUNCTION, RETURN, INT, ELSE, DO, BREAK, END, ASSIGN, ADD, SUB, MUL, DIV, MOD, GT, LT, GE, LE, LP,
	RP, IDENT, STRING, INT_LIT, SEMI, INC, LB, RB, EE, COMMA, NEG, OR, AND
}

public class Lexer {

	private static HashMap<String, Token> wordsAndOperatorsDictionary = new HashMap<String, Token>();

	public static void Tokenize(String filePath) {
		initializeTokens();
		try {
			File inputfile = new File(filePath);
			FileReader fr = new FileReader(inputfile);
			BufferedReader br = new BufferedReader(fr);

			String line;
			while ((line = br.readLine()) != null) {
				analyzeCodeLine(line.strip());
			}

			br.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void analyzeCodeLine(String line) throws Exception {
		if (!line.isEmpty()) {
			String[] lineArr = line.split(" ");
			for (String str : lineArr) {
				Token token = null;

				if (wordsAndOperatorsDictionary.containsKey(str.toLowerCase())) {
					token = wordsAndOperatorsDictionary.get(str.toLowerCase());
					System.out.println(token);
				}

				else
					evaluate(str);
			}
		}

	}

	private static void evaluate(String str) throws Exception {
		String word = "";
		String operator = "";
		char[] chars = str.toCharArray();

		if (chars.length > 1) {
			char nextChar;

			if (isLetterOrDigit(chars[0])) {
				word += chars[0];
			} else {
				operator += chars[0];
			}

			for (int i = 1; i < chars.length; i++) {
				nextChar = chars[i];
				boolean isLastIndex = i == chars.length - 1;
				boolean currentOperatorHasMoreThanOneSymbol = false;

				if (!isLastIndex) {
					boolean canCheckLongOperator = i + 1 <= chars.length - 1;
					boolean isComparisonOperator = (nextChar == '<' || nextChar == '>' || nextChar == '=');

					currentOperatorHasMoreThanOneSymbol = canCheckLongOperator
							&& ((isComparisonOperator && chars[i + 1] == '=')
									|| (nextChar == '+' && chars[i + 1] == '+'));
				}

				if (isLetterOrDigit(nextChar)) {
					word += nextChar;

					if (currentOperatorHasMoreThanOneSymbol) {
						operator = nextChar + "" + chars[i + 1];
						i += 1;
					}

					boolean isValidOperator = analyzeOperatorAndDetermineIfItIsValid(operator);
					operator = "";

					if (!isValidOperator)
						handleSyntaxError();

					if (isLastIndex) {
						boolean isValidIdent = analyzeWordAndDetermineIfItIsValid(word);
						word = "";

						if (!isValidIdent)
							handleSyntaxError();
					}

				} else {
					operator = "" + nextChar;

					if (currentOperatorHasMoreThanOneSymbol) {
						operator = nextChar + "" + chars[i + 1];
						i += 1;
					}

					boolean isValidIdent = analyzeWordAndDetermineIfItIsValid(word);
					word = "";

					if (!isValidIdent)
						handleSyntaxError();

					boolean isValidOperator = analyzeOperatorAndDetermineIfItIsValid(operator);
					operator = "";

					if (!isValidOperator)
						handleSyntaxError();
				}
			}
		} else {
			String character = chars[0] + "";
			boolean isValid = false;

			if (isLetterOrDigit(chars[0])) {
				isValid = analyzeWordAndDetermineIfItIsValid(character);
			} else
				isValid = analyzeOperatorAndDetermineIfItIsValid(character);

			if (!isValid)
				handleSyntaxError();
		}
	}

	private static void handleSyntaxError() throws Exception {
		throw new Exception("SYNTAX ERROR");
	}

	private static boolean isLetterOrDigit(char c) {
		return Character.isLetter(c) || Character.isDigit(c);
	}

	private static boolean analyzeWordAndDetermineIfItIsValid(String word) {

		boolean isValid = true;
		if (!word.isBlank()) {
			String outputLine = "";

			if (wordsAndOperatorsDictionary.containsKey(word.toLowerCase())) {
				Token token = wordsAndOperatorsDictionary.get(word.toLowerCase());
				outputLine = token + "";
			} else {

				if (Character.isLetter(word.charAt(0))) {
					outputLine = Token.IDENT + ": " + word;
				}

				if (Character.isDigit(word.charAt(0))) {

					if (word.matches("^[0-9]*$")) {
						outputLine = Token.INT_LIT + ": " + word;
					} else {
						outputLine = "SYNTAX ERROR: INVALID IDENTIFIER NAME: " + word;
						isValid = false;
					}
				}
			}
			System.out.println(outputLine);
		}

		return isValid;
	}

	private static boolean analyzeOperatorAndDetermineIfItIsValid(String operator) {

		boolean isValid = true;
		if (!operator.isBlank()) {
			String outputLine = "";

			if (wordsAndOperatorsDictionary.containsKey(operator.toLowerCase())) {
				Token token = wordsAndOperatorsDictionary.get(operator.toLowerCase());
				outputLine = token + "";

			} else {
				outputLine = "SYNTAX ERROR: INVALID OPERATOR: " + operator;
				isValid = false;
			}

			System.out.println(outputLine);

		}

		return isValid;
	}

	private static void initializeTokens() {

		try {
			wordsAndOperatorsDictionary.put("if", Token.IF);
			wordsAndOperatorsDictionary.put("for", Token.FOR);
			wordsAndOperatorsDictionary.put("while", Token.WHILE);
			wordsAndOperatorsDictionary.put("function", Token.FUNCTION);
			wordsAndOperatorsDictionary.put("return", Token.RETURN);
			wordsAndOperatorsDictionary.put("int", Token.INT);
			wordsAndOperatorsDictionary.put("else", Token.ELSE);
			wordsAndOperatorsDictionary.put("do", Token.DO);
			wordsAndOperatorsDictionary.put("break", Token.BREAK);
			wordsAndOperatorsDictionary.put("end", Token.END);
			wordsAndOperatorsDictionary.put("=", Token.ASSIGN);
			wordsAndOperatorsDictionary.put("+", Token.ADD);
			wordsAndOperatorsDictionary.put("-", Token.SUB);
			wordsAndOperatorsDictionary.put("*", Token.MUL);
			wordsAndOperatorsDictionary.put("/", Token.DIV);
			wordsAndOperatorsDictionary.put("%", Token.MOD);
			wordsAndOperatorsDictionary.put(">", Token.GT);
			wordsAndOperatorsDictionary.put("<", Token.LT);
			wordsAndOperatorsDictionary.put(">=", Token.GE);
			wordsAndOperatorsDictionary.put("<=", Token.LE);
			wordsAndOperatorsDictionary.put("++", Token.INC);
			wordsAndOperatorsDictionary.put("(", Token.LP);
			wordsAndOperatorsDictionary.put(")", Token.RP);
			wordsAndOperatorsDictionary.put("{", Token.LB);
			wordsAndOperatorsDictionary.put("}", Token.RB);
			wordsAndOperatorsDictionary.put("|", Token.OR);
			wordsAndOperatorsDictionary.put("&", Token.AND);
			wordsAndOperatorsDictionary.put("==", Token.EE);
			wordsAndOperatorsDictionary.put("!", Token.NEG);
			wordsAndOperatorsDictionary.put(",", Token.COMMA);
			wordsAndOperatorsDictionary.put(";", Token.SEMI);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
