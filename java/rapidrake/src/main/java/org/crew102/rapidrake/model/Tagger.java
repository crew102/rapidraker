package org.crew102.rapidrake.model;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class Tagger {
	
	public static POSTaggerME posTagger;
	private static String inputString;
	
	public Tagger(String inputString) {
		this.inputString = inputString;
	}
	public Tagger() {
		this.inputString = "model-bin/en-pos-maxent.bin";
	}
		
	// Load utils
	static {
		InputStream modelIn2;
		POSModel model2 = null;
		
		try {
			modelIn2 = new FileInputStream(inputString);
			model2 = new POSModel(modelIn2);
		} catch (IOException ex) {
			// need to handle exception
		}
		posTagger = new POSTaggerME(model2);
	}
	

}