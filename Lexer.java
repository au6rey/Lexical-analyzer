import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;

enum Token {
	IF, FOR, WHILE, FUNCTION, RETURN, INT, ELSE, DO, BREAK, END, ASSIGN, ADD, SUB, MUL, DIV, MOD, GT, LT, GE, LE, LP,
	RP, IDENT, STRING, INT_LIT, FLOAT, INVALID, SEMI, INC, LB, RB
}

public class Lexer {

	private static HashMap<String, Token> wordsAndOperatorsDictionary = new HashMap<String, Token>();

	public static void Tokenize(String filePath) {
		initializeTokens();
		try {
			File file = new File(filePath);
			FileReader fr;
			fr = new FileReader(file);
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
			wordsAndOperatorsDictionary.put("++", Token.INC);
			wordsAndOperatorsDictionary.put("-", Token.SUB);
			wordsAndOperatorsDictionary.put("*", Token.MUL);
			wordsAndOperatorsDictionary.put("/", Token.DIV);
			wordsAndOperatorsDictionary.put("%", Token.MOD);
			wordsAndOperatorsDictionary.put(">", Token.GT);
			wordsAndOperatorsDictionary.put("<", Token.LT);
			wordsAndOperatorsDictionary.put(">=", Token.GE);
			wordsAndOperatorsDictionary.put("<=", Token.LE);
			wordsAndOperatorsDictionary.put("(", Token.LP);
			wordsAndOperatorsDictionary.put(")", Token.RP);
			wordsAndOperatorsDictionary.put("{", Token.LB);
			wordsAndOperatorsDictionary.put("}", Token.RB);
			wordsAndOperatorsDictionary.put(";", Token.SEMI);
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
					evaluate(str, token);
			}
		}

	}

	private static void evaluate(String str, Token previousToken) throws Exception {
		String word = "";
		String operator = "";
		char[] chars = str.toCharArray();
		if (chars.length > 1) {
			char nextChar;
			if ((Character.isLetter(chars[0]) || Character.isDigit(chars[0]))) {
				word += chars[0];
			} else {
				operator += chars[0];
			}

			for (int i = 1; i < chars.length; i++) {
				nextChar = chars[i];
				boolean isLastIndex = i == chars.length - 1;
				boolean canCheckLongOperator = i <= chars.length - 1;
				boolean currentOperatorHasMoreThanTwoSymbols = (((nextChar == '<' || nextChar == '>')
						&& chars[i + 1] == '=') || (nextChar == '+' && chars[i + 1] == '+'));
				if ((Character.isLetter(nextChar) || Character.isDigit(nextChar))) {
					word += nextChar;

					if (canCheckLongOperator && currentOperatorHasMoreThanTwoSymbols) {
						operator = nextChar + "" + chars[i + 1];
						i += 1;
					}

					boolean isValidOperator = analyzeOperatorAndDetermineIfItIsValid(operator);
					operator = "";

					if (!isValidOperator)
						handleSyntaxError();

					boolean isValidIdent = false;
					if (isLastIndex) {
						isValidIdent = analyzeWordAndDetermineIfItIsValid(word);
						word = "";
						if (!isValidIdent) {
							handleSyntaxError();
						}
					}

				} else {
					operator = "" + nextChar;
					if (canCheckLongOperator && currentOperatorHasMoreThanTwoSymbols) {
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
			String word2 = chars[0] + "";
			boolean isValidIdent = analyzeWordAndDetermineIfItIsValid(word2);
			if (!isValidIdent) {
				analyzeOperatorAndDetermineIfItIsValid(word2);
			}
			word2 = "";
		}
	}

	private static void handleSyntaxError() throws Exception {
		throw new Exception("SYNTAX ERROR");
	}

	private static boolean analyzeWordAndDetermineIfItIsValid(String word) {
		if (!word.isBlank()) {
			if (wordsAndOperatorsDictionary.containsKey(word.toLowerCase())) {
				Token token = wordsAndOperatorsDictionary.get(word.toLowerCase());
				System.out.println(token);
			} else {
				if (Character.isLetter(word.charAt(0))) {
					System.out.println(Token.IDENT + ": " + word);
				}
				if (Character.isDigit(word.charAt(0))) {
					if (word.matches("^[0-9]*$")) {
						System.out.println(Token.INT_LIT + ": " + word);
					} else {
						System.out.println("SYNTAX ERROR: INVALID IDENTIFIER NAME: " + word);
						return false;
					}
				}
			}
			word = "";
		}
		return true;
	}

	private static boolean analyzeOperatorAndDetermineIfItIsValid(String operator) {
		if (!operator.isBlank()) {
			if (wordsAndOperatorsDictionary.containsKey(operator.toLowerCase())) {
				Token token = wordsAndOperatorsDictionary.get(operator.toLowerCase());
				System.out.println(token);
			} else {
				System.out.println("INVALID OPERATOR");
				return false;
			}
			operator = "";
		}

		return true;
	}

}
