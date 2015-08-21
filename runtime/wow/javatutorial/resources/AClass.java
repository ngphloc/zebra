public class AClass {

    public int instanceInteger = 0;
    public int instanceMethod() {
        return instanceInteger;
    }

    public static int classInteger = 0;
    public static int classMethod() {
        return classInteger;
    }

    public static void main(String[] args) {
        AClass anInstance = new AClass();
        AClass anotherInstance = new AClass();

        //Refer to instance members through an instance.
        anInstance.instanceInteger = 1;
        anotherInstance.instanceInteger = 2;
        System.out.println(anInstance.instanceMethod());
        System.out.println(anotherInstance.instanceMethod());

        //Illegal to refer directly to instance members from a class method
        //System.out.println(instanceMethod());    //illegal
        //System.out.println(instanceInteger);     //illegal

        //Refer to class members through the class...
        AClass.classInteger = 7;
        System.out.println(classMethod());

        //...or through an instance.
        System.out.println(anInstance.classMethod());

	//Instances share class variables
        anInstance.classInteger = 9;
        System.out.println(anInstance.classMethod());
        System.out.println(anotherInstance.classMethod());
    }
}
