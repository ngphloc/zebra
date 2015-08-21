public class Class2 extends Class1 {
    public Class2() {
	ic = new InnerClass2();
    }

    static public void main(String[] args) {
        Class2 c2 = new Class2();
	c2.displayStrings();
    }

    protected class InnerClass2 extends InnerClass1 {
	public String getAnotherString() {
	    return "InnerClass2 version of getAnotherString invoked";
	}
    }

}
