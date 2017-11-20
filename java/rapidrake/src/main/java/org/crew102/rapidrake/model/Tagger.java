package org.crew102.rapidrake.model;

import java.io.FileInputStream;
import java.io.InputStream;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class Tagger {
	
	private String inputString;
	
	// For when we pass URL of binary that is found in openNLPdata R package (i.e., when app is called from R)
	public Tagger(String inputString) {
		this.inputString = inputString;
	}
	// For when app is not called from R...You have to download en-pos-maxent.bin to model-bin before running app
	public Tagger() {
		this.inputString = "model-bin/en-pos-maxent.bin";
	}
	
	public POSTaggerME getPosTagger() throws java.io.IOException {
		InputStream modelIn2 = new FileInputStream(inputString);	
		POSModel model2 = new POSModel(modelIn2);
		return new POSTaggerME(model2);
	}
		
}