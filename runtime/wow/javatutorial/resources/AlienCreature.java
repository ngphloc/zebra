abstract class AlienCreature {    
    // all creaturesd are born
    String lifeBegins(){
        String me="Hello, I'm here.";
        return me;
    }
    // creatures live by eating different material..so this is abstract
    abstract String live();
    
    // all creatures die
    String lifeEnds(){
        String meEnd="Oh no, it all ends - bye. ";
        return meEnd;
    }   
}
 
