public class StackTest {
	public static void main(String[] args) {
		Stack s = new Stack(3);
		s.push("hi");
		s.push("hello");
		s.push("good day");
		Stack s2 = s.clone();
		System.out.println(s.pop());
		System.out.println(s.pop());
		System.out.println(s.pop());
		System.out.println(s2.pop());
	}
}
