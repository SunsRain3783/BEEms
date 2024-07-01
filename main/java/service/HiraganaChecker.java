package service;

public class HiraganaChecker {

	// 文字列がひらがなであるかを判定するメソッド
	public static boolean isHiragana(String str) {
		for (char c : str.toCharArray()) {
			if (!isHiraganaChar(c)) {
				return false;
			}
		}
		return true;
	}

	// 文字がひらがなであるかを判定するメソッド
	private static boolean isHiraganaChar(
			char c) {
		return (c >= '\u3040'
				&& c <= '\u309F') || c == '\u30FC';
	}
}