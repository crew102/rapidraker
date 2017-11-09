package org.crew102.rapidrake;

import org.crew102.rapidrake.model.*;

// A RakeWrapper creates a RakeAlg obj and calls rake on Documents

public class RakeWrapper { 
	
	// For testing
	public static void main(String[] args) throws java.io.IOException {
		RakeAlgorithm alg = new RakeAlgorithm();
		Result out = alg.rake("this is a fun thing to do, right?");
		
	}
}