  

// check out classes
public class AbstractClassTest {
    public static void main(String[] args) {
        
        // check out the class alien one
        AlienCreatureOne aOne=new AlienCreatureOne();
        System.out.println(aOne.lifeBegins());
        System.out.println(aOne.live());
        System.out.println(aOne.lifeEnds());
        
        // check out the class alien two
        AlienCreatureTwo aTwo=new AlienCreatureTwo();
        System.out.println(aTwo.lifeBegins());
        System.out.println(aTwo.live());
        System.out.println(aTwo.lifeEnds());
    }
    
}
