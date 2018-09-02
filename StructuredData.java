package DecisionTree;

import java.util.ArrayList;
import java.util.HashMap;

public class StructuredData {
	ArrayList<ArrayList<Integer>> dataSet;
	ArrayList<Integer> classAttribute;
	HashMap<String,Integer> attributes;
	public ArrayList<ArrayList<Integer>> getDataSet() {
		return dataSet;
	}
	public void setDataSet(ArrayList<ArrayList<Integer>> dataSet) {
		this.dataSet = dataSet;
	}
	public ArrayList<Integer> getClassAttribute() {
		return classAttribute;
	}
	public void setClassAttribute(ArrayList<Integer> classAttribute) {
		this.classAttribute = classAttribute;
	}
	public HashMap<String, Integer> getAttributes() {
		return attributes;
	}
	public void setAttributes(HashMap<String, Integer> attributes) {
		this.attributes = attributes;
	}
	
	
}
