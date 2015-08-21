import java.util.*;
public class Cats {
	public static void main(String[] args) {
		Cat myCat = new Cat();
		Collection<Cat> litter = myCat.getLitter(3);
		for (Cat c : litter)
			System.out.println(c.getColor());
	}
}
