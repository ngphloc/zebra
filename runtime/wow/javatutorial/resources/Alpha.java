package One;

public class Alpha {

    //member variables
    private   int iamprivate = 1;
              int iampackage = 2;       //package access
    protected int iamprotected = 3;
    public    int iampublic = 4;


    //methods
    private    void privateMethod() {
        System.out.println("iamprivate Method");
    }
    /*       */void packageMethod() {   //package access
        System.out.println("iampackage Method");
    }
    protected  void protectedMethod() {
        System.out.println("iamprotected Method");
    }
    public     void publicMethod() {
        System.out.println("iampublic Method");
    }

    public static void main(String[] args) {
        Alpha a = new Alpha();
        a.privateMethod();    //legal
        a.packageMethod();    //legal
        a.protectedMethod();  //legal
        a.publicMethod();     //legal

        System.out.println("iamprivate: " + a.iamprivate);       //legal
        System.out.println("iampackage: " + a.iampackage);       //legal
        System.out.println("iamprotected: " + a.iamprotected);   //legal
        System.out.println("iampublic: " + a.iampublic);         //legal
    }
}
