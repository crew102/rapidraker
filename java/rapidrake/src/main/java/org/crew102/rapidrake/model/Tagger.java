package org.crew102.rapidrake.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;

public class Tagger {
	
	private String inputString;
	
	public Tagger(String inputString) {
		this.inputString = inputString;
	}
	public Tagger() {
		this.inputString = "model-bin/en-pos-maxent.bin";
	}
	
	public POSTaggerME getPosTagger() throws java.io.IOException {
		InputStream modelIn2 = new FileInputStream(inputString);
		
		File fi = new File(inputString);
		System.out.println(fi.exists());
		POSModel model2 = new POSModel(modelIn2);
		return new POSTaggerME(model2);
	}
		
}