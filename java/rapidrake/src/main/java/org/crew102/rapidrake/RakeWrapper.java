package org.crew102.rapidrake;

import java.util.ArrayList;
import org.crew102.rapidrake.model.*;

// A RakeWrapper creates a RakeAlg obj and calls rake on Documents

public class RakeWrapper { 
	
	// For testing
	public static void main(String[] args) throws java.io.IOException {
		RakeAlgorithm alg = new RakeAlgorithm();
		Document aD = new Document("this is a fun thing to do, right?");
		Result out = alg.rake(aD);
		// create outKeys custom object that is arrayList of keywords but
		// has method to convert to array of prim[] for each data attribute
		// and has get methods for each prim[]..basically it's a container for the keywords
		
	}
}