import java.util.*;

public class Stack2<T> implements Cloneable {
		private ArrayList<T> items;
		private int top=0;

    public Stack2() {
			items = new ArrayList<T>();
		}

    public void push(T item) {
			items.add(item);
			top++;
    }

    public T pop() {
      if (items.size() == 0)
          throw new EmptyStackException();
      T obj = items.get(--top);
      return obj;
    }

    public boolean isEmpty() {
      if (items.size() == 0)
         return true;
      else
         return false;
    }

    protected Stack2<T> clone() {
      try {
         Stack2<T> s = (Stack2<T>)super.clone(); // Clone the stack
         s.items =  (ArrayList<T>)items.clone(); // Clone the list
         return s; // Return the clone
      } catch (CloneNotSupportedException e) {
         //This shouldn't happen because Stack is Cloneable
         throw new InternalError();
      }
   }
}

