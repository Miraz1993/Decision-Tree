package DecisionTree;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;



public class BuildDecisionTree{

	public static StructuredData dataFormatting(String in) {
		
		//This method is used to properly structure the data from csv file. Output of the method is an object 
		//of StructuredData class in which the attributes, the class values and input values are structured 
		//differently.
		BufferedReader bufferedReader = null;
		try {
			bufferedReader=new BufferedReader(new FileReader(in));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ArrayList<ArrayList<Integer>> dataSet = new ArrayList<ArrayList<Integer>>();
		String line="";
		String[] strArr;
		ArrayList<Integer> eachRow;
		ArrayList<Integer> classAttribute=new ArrayList<>();
		int ind=0;
		HashMap<String,Integer> attributes =new HashMap<>();
		try {
			while ((line = bufferedReader.readLine()) != null) {
				strArr=line.split(",");
				eachRow=new ArrayList<>();
				if(ind==0) {
					for(int i=0;i<strArr.length-1;i++) {

						attributes.put(strArr[i],i);
						

					}
				}
				else {
					for(int i=0;i<strArr.length-1;i++) {

						eachRow.add(Integer.parseInt(strArr[i]));

					}
					classAttribute.add(Integer.parseInt(strArr[strArr.length-1]));

				}
				if(ind!=0)
					dataSet.add(eachRow);
				ind++;


			}


			StructuredData sData=new StructuredData();
			sData.setAttributes(attributes);
			sData.setClassAttribute(classAttribute);
			sData.setDataSet(dataSet);
			return sData;
		} catch (NumberFormatException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		int l=0,k=0;

		try {
			
			l=Integer.parseInt(args[0]);
			k=Integer.parseInt(args[1]);

			String trainingSet=args[2];
			String validationSet=args[3];
			String testSet=args[4];
			String printV=args[5];
			if(!printV.equalsIgnoreCase("yes") && !printV.equalsIgnoreCase("no")) {
				throw new InputMismatchException();
			}

			StructuredData sData;
			DecisionTreeOperations dtOperations=new DecisionTreeOperations();
			
			double per=0.0;
			
			System.out.println("Accuracy values for Information Gain Heuristic ");
			System.out.println();
			sData=dataFormatting(trainingSet);

			DecisionNode node=dtOperations.formingDecisionTree(sData.getDataSet(), sData.getClassAttribute(), sData.getAttributes());
			

			if(printV.equalsIgnoreCase("yes")) {
				System.out.println("The tree for Information Gain Heuristic");
				System.out.println("");
				dtOperations.printTree(node);
			}

			
			sData=dataFormatting(validationSet);

			DecisionNode temp=dtOperations.postPruning(sData.getDataSet(), node, l, k, sData.getClassAttribute(), sData.getAttributes());
			
			sData=dataFormatting(trainingSet);

			per=percAccuracyCheck(temp, dtOperations, sData);

			System.out.println("Accuracy for trainingSet "+per);

			sData=dataFormatting(validationSet);

			per=percAccuracyCheck(temp, dtOperations, sData);

			System.out.println("Accuracy for validationSet "+per);

			sData=dataFormatting(testSet);

			per=percAccuracyCheck(temp, dtOperations, sData);

			System.out.println("Accuracy for testSet "+per);


			
			sData=dataFormatting(trainingSet);
			node=dtOperations.formingDecisionTreeUsingVar(sData.getDataSet(), sData.getClassAttribute(), sData.getAttributes());



			
			sData=dataFormatting(validationSet);

			temp=dtOperations.postPruning(sData.getDataSet(), node, l, k, sData.getClassAttribute(), sData.getAttributes());
			System.out.println("--------------------");
			System.out.println("Accuracy values for Variance Impurity Heuristic ");
			System.out.println();
			if(printV.equalsIgnoreCase("yes")) {
				System.out.println("The tree for Variance Impurity Heuristic");
				System.out.println("");
				dtOperations.printTree(node);
				
			}

			
			sData=dataFormatting(trainingSet);

			per=percAccuracyCheck(temp, dtOperations, sData);

			System.out.println("Accuracy for trainingSet "+per);

			sData=dataFormatting(validationSet);

			per=percAccuracyCheck(temp, dtOperations, sData);

			System.out.println("Accuracy for validationSet "+per);

			sData=dataFormatting(testSet);

			per=percAccuracyCheck(temp, dtOperations, sData);

			System.out.println("Accuracy for testSet "+per);

			


			
		}
		catch(NumberFormatException n) {
			System.out.println("Value of L and K should be integer");
		}
		catch(InputMismatchException e) {
			
			System.out.println("value of printValue should be yes or no");
			
		}



	}

	private static double percAccuracyCheck(DecisionNode node, DecisionTreeOperations dtOperations, StructuredData sData) {
		//This method takes the whole dataset and compute accuracy thorough calling another method which 
		//takes each row and check it is matching with the decision tree or not.
		double tot=0;
		
		for(int i=0;i<sData.getDataSet().size();i++) {

			Integer res=dtOperations.accuracyCheck(sData.getDataSet().get(i), node, sData.getAttributes());
			
			if(res.equals(sData.getClassAttribute().get(i)))
				tot+=1;

		}

		
		double per=(tot*100)/sData.getDataSet().size();
		return per;
	}

}
