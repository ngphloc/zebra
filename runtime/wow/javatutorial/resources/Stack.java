import java.util.EmptyStackException;

public class Stack implements Cloneable {
    private Object[] items;
    private int top = 0;

    public Stack(int size) {
			items = new Object[size];
    }

    public void push(Object item) {
			items[top++] = item;
    }

    public Object pop() {
      if (top == 0)
          throw new EmptyStackException();
      Object obj = items[--top];
			items[top]=null;
      return obj;
    }

    public boolean isEmpty() {
      if (top == 0)
         return true;
      else
         return false;
    }
    
    protected Stack clone() {
      try {
         Stack s = (Stack)super.clone(); // Clone the stack
         s.items = (Object[]) items.clone(); // Clone the list
				 s.top = top; // Clone the array index of the top
         return s; // Return the clone
      } catch (CloneNotSupportedException e) {
         //This shouldn't happen because Stack is Cloneable
         throw new InternalError();
      }
   }
}
