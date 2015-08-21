//Will not run standalone. See entire Bingo example:
//http://java.sun.com/docs/books/tutorial/together/bingo/index.html

package bingo.game;

import java.util.*;

import bingo.shared.*;

class RandomBag implements BagOfBalls {

    private boolean DEBUG = false;
    private Vector balls = new Vector(BingoBall.MAX);
    private Random generator = new Random(System.currentTimeMillis());

    RandomBag () {
	for (int j = BingoBall.MIN; j <= BingoBall.MAX; j++)
	    balls.addElement(new BingoBall(j));
    }

    public BingoBall getNext() throws NoMoreBallsException {
	if (balls.size() > 0) {
	    int num = (int)(generator.nextDouble() * balls.size());
	    BingoBall returnThis = (BingoBall)balls.elementAt(num);
	    balls.removeElementAt(num);
	    return returnThis;
	} else {
	    throw new NoMoreBallsException();
	}
    }
}
