import java.util.*;
public class Cat extends Animal {

		public Cat() {
			super.setColor("white");
		}
		
		public static void hide() {
        System.out.println("The hide method in Cat.");
    }
    public void override() {
        System.out.println("The override method in Cat.");
    }

		// Returns a litter of cats
		public Collection getLitter(int size) {
			ArrayList litter = new ArrayList(size);
			for (int i = 0; i < size; i++) 
				litter.add(i, new Cat());
			return litter;
		}
		
    public static void main(String[] args) {
        Cat myCat = new Cat();
        Animal myAnimal = (Animal)myCat;
        myAnimal.hide();                   
        myAnimal.override();
    }
}
