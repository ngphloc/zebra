public class StackTest2 {
	public static void main(String[] args) {
		Stack2<String> s = new Stack2<String>();
		s.push("hi");
		s.push("hello");
		s.push("good day");
		Stack2<String> s2 = s.clone();
		System.out.println(s.pop());
		System.out.println(s.pop());
		System.out.println(s.pop());
		System.out.println(s2.pop());
	}
}
