public class InsertDemo {
    public static void main(String[] args) {
        StringBuffer palindrome = new StringBuffer(
                        "A man, a plan, a canal; Panama.");
        palindrome.insert(15, "a cat, ");
        System.out.println(palindrome);
    }
}
