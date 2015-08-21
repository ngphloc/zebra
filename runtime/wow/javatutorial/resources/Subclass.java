public class Subclass extends Superclass {
    public boolean aVariable;       //hides aVariable in Superclass
    public void aMethod() {         //overrides aMethod in Superclass
        aVariable = false;
        super.aMethod();
        System.out.println(aVariable);
        System.out.println(super.aVariable);
    }
}