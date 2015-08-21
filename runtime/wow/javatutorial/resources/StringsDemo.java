public class StringsDemo {
	public static void main(String[] args) {
		String palindrome = "Dot saw I was Tod";
		int len = palindrome.length();
		StringBuilder dest = new StringBuilder(len);
		for (int i = (len - 1); i >= 0; i--) {
			dest.append(palindrome.charAt(i));
		}
		System.out.println(dest.toString());
	}
}
