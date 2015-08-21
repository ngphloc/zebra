package Two;
import One.*;

public class DeltaTwo {
    public static void main(String[] args) {
        Alpha a = new Alpha();
      //a.privateMethod();    //illegal
      //a.packageMethod();    //illegal
      //a.protectedMethod();  //illegal
        a.publicMethod();     //legal

      //System.out.println("iamprivate: " + a.iamprivate);       //illegal
      //System.out.println("iampackage: " + a.iampackage);       //illegal
      //System.out.println("iamprotected: " + a.iamprotected);   //illegal
        System.out.println("iampublic: " + a.iampublic);         //legal
    }
}
