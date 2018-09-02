package DecisionTree;

//import java.security.AllPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;



public class DecisionTreeOperations {
	
	private static int printIndex=-1;
	
	private static int nonLeafCount=0;
	
	public static HashMap<Integer, DecisionNode> nonLeafNodes=new HashMap<>();
	


	private String selectingBestAttribute(ArrayList<ArrayList<Integer>> trainingSet, ArrayList<Integer> classAttribute,HashMap<String,Integer> attributes) {
		//This method is used to pick the best attribute for IG heuristic each time when training set is sent
		String bestAttribute = "";
		Double maxGain= -Double.MAX_VALUE;



		for(String attr:attributes.keySet()) {
			

			Double infoGain=informationGain(trainingSet, attributes.get(attr), attributes, classAttribute);
			int retVal=Double.compare(infoGain,maxGain);
			if(retVal>0) {

				maxGain=infoGain;
				bestAttribute=attr;


				

			}


		}
		
		
		return bestAttribute;

	}

	private String selectingBestAttributeVariance(ArrayList<ArrayList<Integer>> trainingSet, ArrayList<Integer> classAttribute,HashMap<String,Integer> attributes) {
		//This method is used to pick the best attribute for VI heuristic each time when training set is sent
		String bestAttribute = "";
		Double maxGain=-Double.MAX_VALUE;

		



		for(String attr:attributes.keySet()) {

			Double infoGainVar=informationGainVariance(trainingSet, attributes.get(attr), attributes, classAttribute);
			
			int retVal=Double.compare(infoGainVar,maxGain);
			if(retVal>0) {
				
				maxGain=infoGainVar;
				
				bestAttribute=attr;

			}


		}
		
		return bestAttribute;

	}


	private ArrayList<Integer> getSubsetIndex(ArrayList<ArrayList<Integer>> trainingSet,int attribute,int value){
		//This method takes a particular value of an attribute and sends back all the row indexes in which
		//the value is present.
		ArrayList<Integer> index=new ArrayList<Integer>();

		for(int i=0;i<trainingSet.size();i++) {

			if(trainingSet.get(i).get(attribute).equals(value)) {
				index.add(i);
			}
		}




		return index;

	}


	private Double informationGain(ArrayList<ArrayList<Integer>> trainingSet, int attribute,HashMap<String,Integer> attributes,ArrayList<Integer> classAttribute) {
		//This method calculates information gain.
		HashMap<Integer, Double> valueMap=new HashMap<>();

		for(ArrayList<Integer> list:trainingSet) {
			
			if(valueMap.containsKey(list.get(attribute))) {
				valueMap.put(list.get(attribute), valueMap.get(list.get(attribute))+1);
			}
			else {
				valueMap.put(list.get(attribute), 1.0);
			}
		}

		double subEntropy=0.0;
		for(int i:valueMap.keySet()) {
			
			Double valueFreq=(valueMap.get(i)/trainingSet.size());

			ArrayList<Integer> index=getSubsetIndex(trainingSet, attribute, i);
			ArrayList<ArrayList<Integer>> subSet = new ArrayList<ArrayList<Integer>>();
			ArrayList<Integer> targetSubset = new ArrayList<>();

			for(Integer ind:index) {

				
				subSet.add(trainingSet.get(ind));
				targetSubset.add(classAttribute.get(ind));
			}


			
			subEntropy+=valueFreq*entropyCalculator(targetSubset);

			

		}


		double entropy=entropyCalculator(classAttribute);
		
		double infoGain=entropy-subEntropy;
		
		return infoGain;


	}


	private Double informationGainVariance(ArrayList<ArrayList<Integer>> trainingSet, int attribute,HashMap<String,Integer> attributes,ArrayList<Integer> classAttribute) {
		//This method caluculates variance impurity gain.
		HashMap<Integer, Double> valueMap=new HashMap<>();

		for(ArrayList<Integer> list:trainingSet) {

			if(valueMap.containsKey(list.get(attribute))) {
				valueMap.put(list.get(attribute), valueMap.get(list.get(attribute))+1);
			}
			else {
				valueMap.put(list.get(attribute), 1.0);
			}
		}

		Double subVar=0.0;
		for(int i:valueMap.keySet()) {
			Double valueFreq=(valueMap.get(i)/trainingSet.size());

			ArrayList<Integer> index=getSubsetIndex(trainingSet, attribute, i);
			ArrayList<ArrayList<Integer>> subSet = new ArrayList<ArrayList<Integer>>();
			ArrayList<Integer> targetSubset = new ArrayList<>();

			for(Integer ind:index) {
				subSet.add(trainingSet.get(ind));
				targetSubset.add(classAttribute.get(ind));
			}

			subVar=subVar+valueFreq*varianceCalculator(targetSubset);
			

		}


		Double var=varianceCalculator(classAttribute);
		
		Double infoGain=var-subVar;
		

		return infoGain;


	}

	private double entropyCalculator(ArrayList<Integer> classAttribute) {
		//this method calculates the entropy.
		double entropy=0.0;
		HashMap<Integer, Integer> valueMap=new HashMap<>();
		for(Integer value:classAttribute) {

			if(valueMap.containsKey(value)) {
				valueMap.put(value, valueMap.get(value)+1);
			}
			else {
				valueMap.put(value, 1);
			}



		}

		for(Integer value:valueMap.keySet()) {
			
			double prob=(double)(valueMap.get(value))/(double)(classAttribute.size());
			
			double logValue=Math.log(prob)/Math.log(2);
			
			entropy+=(-prob)*logValue;
			

		}
		
		return entropy;

	}

	private Double varianceCalculator(ArrayList<Integer> classAttribute) {
		//This method calculates the variance impurity.
		Double num=1.0;

		HashMap<Integer, Integer> valueMap=new HashMap<>();
		for(Integer value:classAttribute) {

			if(valueMap.containsKey(value)) {
				valueMap.put(value, valueMap.get(value)+1);
			}
			else {
				valueMap.put(value, 1);
			}



		}
		Integer totVal=0;
		Integer zeroCount=0,oneCount=0;
		for(Integer value:valueMap.keySet()) {
			totVal+=valueMap.get(value);
			

			if(value==0)
				zeroCount=valueMap.get(value);
			else
				oneCount=valueMap.get(value);

		}
		
		
		num=(((double)zeroCount/totVal)*((double)oneCount/totVal));
		
		return num;

	}

	public DecisionNode formingDecisionTree(ArrayList<ArrayList<Integer>> trainingSet, ArrayList<Integer> classAttribute,HashMap<String,Integer> attributes) {
		//This method is called first from main method for IG heuristic. It calls itself recursively
		//until and unless we get pure values or attributes are finished.
		Integer[] majority=majorityValue(classAttribute);
		
		if(trainingSet.size()==0 || attributes.size()<1 || majority[1]==classAttribute.size()) {
			
			return new DecisionNode("Class", majority[0]);
		}


		else {

			
			String bestAttr=selectingBestAttribute(trainingSet, classAttribute, attributes);
			

			if(bestAttr=="")
				return new DecisionNode("Class", majority[0]);


			
			
			HashMap<String,Integer> remainingAttributes=new HashMap<String,Integer>();
			for(String attr:attributes.keySet()) {
				if(!attr.equals(bestAttr)) {
					
					remainingAttributes.put(attr,attributes.get(attr));
				}

			}

			

			DecisionNode bestDecisionNode=new DecisionNode(bestAttr);
			HashSet<Integer> valueSet=new HashSet<>();
			for(ArrayList<Integer> row:trainingSet) {
				valueSet.add(row.get(attributes.get(bestAttr)));

			}

			for(Integer value:valueSet) {
				ArrayList<ArrayList<Integer>> subSet=new ArrayList<ArrayList<Integer>>();
				ArrayList<Integer> targetSubset=new ArrayList<Integer>();
				ArrayList<Integer> indexes=getSubsetIndex(trainingSet, attributes.get(bestAttr), value);
				for(Integer in:indexes) {
					
					subSet.add(trainingSet.get(in));
					targetSubset.add(classAttribute.get(in));
				}
				
				DecisionNode childTree=formingDecisionTree(subSet, targetSubset, remainingAttributes);

				HashMap<Integer, DecisionNode> childNode=new HashMap<>();
				if(bestDecisionNode.getChildNodes()==null || bestDecisionNode.getChildNodes().isEmpty()) {
					childNode.put(value, childTree);
				}

				else {
					childNode=bestDecisionNode.getChildNodes();
					childNode.put(value, childTree);
				}

				bestDecisionNode.setChildNodes(childNode);
			}

			return bestDecisionNode;

		}


	}

	public DecisionNode formingDecisionTreeUsingVar(ArrayList<ArrayList<Integer>> trainingSet, ArrayList<Integer> classAttribute,HashMap<String,Integer> attributes) {
		//This method is called first from main method for VI heuristic. It calls itself recursively
		//until and unless we get pure values or attributes are finished.
		Integer[] majority=majorityValue(classAttribute);
		
		if(trainingSet.size()==0 || attributes.size()<1 || majority[1]==classAttribute.size()) {
			return new DecisionNode("Class", majority[0]);
		}


		else {
			
			String bestAttr=selectingBestAttributeVariance(trainingSet, classAttribute, attributes);
			
			if(bestAttr=="") {
				
				return new DecisionNode("Class", majority[0]);

			}


			
			HashMap<String,Integer> remainingAttributes=new HashMap<String,Integer>();
			for(String attr:attributes.keySet()) {
				if(!attr.equals(bestAttr)) {
					
					remainingAttributes.put(attr,attributes.get(attr));
				}

			}

			

			DecisionNode bestDecisionNode=new DecisionNode(bestAttr);
			HashSet<Integer> valueSet=new HashSet<>();
			for(ArrayList<Integer> row:trainingSet) {
				valueSet.add(row.get(attributes.get(bestAttr)));

			}

			for(Integer value:valueSet) {
				ArrayList<ArrayList<Integer>> subSet=new ArrayList<ArrayList<Integer>>();
				ArrayList<Integer> targetSubset=new ArrayList<Integer>();
				ArrayList<Integer> indexes=getSubsetIndex(trainingSet, attributes.get(bestAttr), value);
				for(Integer in:indexes) {
					subSet.add(trainingSet.get(in));
					targetSubset.add(classAttribute.get(in));
				}



				DecisionNode childTree=formingDecisionTreeUsingVar(subSet, targetSubset, remainingAttributes);
				HashMap<Integer, DecisionNode> childNode=new HashMap<>();
				if(bestDecisionNode.getChildNodes()==null || bestDecisionNode.getChildNodes().isEmpty()) {
					childNode.put(value, childTree);
				}

				else {
					childNode=bestDecisionNode.getChildNodes();
					childNode.put(value, childTree);
				}

				bestDecisionNode.setChildNodes(childNode);

			}


			return bestDecisionNode;

		}


	}

	public static void printTree(DecisionNode node) {

		//this is for printing the tree

		if(node.getChildNodes()==null || node.getChildNodes().isEmpty()) {
			System.out.println(node.getValue());
			
			return;

		}


		
		int j=0;

		

		printIndex++;
		for(Integer in:node.getChildNodes().keySet()) {
			
			if(j==0)
				System.out.println("");
			for(int i=0;i<printIndex;i++) {
				System.out.print("|  ");
			}

			System.out.print(node.getId()+" = ");
			System.out.print(in+" : ");
			printTree(node.getChildNodes().get(in));
			j++;
		}
		printIndex--;
		

	}


	private Integer[] majorityValue(ArrayList<Integer> classAttribute) {

		//This method returns the majority value of class
		HashMap<Integer, Integer> map=new HashMap<>();
		for(Integer i:classAttribute) {
			if(map.containsKey(i)) {
				map.put(i, map.get(i)+1);

			}
			else
				map.put(i, 1);

		}
		int index=-1,max=-1;
		for(Integer i:map.keySet()) {


			if(map.get(i)>max) {
				index=i;
				max=map.get(i);
			}
		}

		Integer[] majority=new Integer[2];
		majority[0]=index;
		majority[1]=max;

		return majority;

	}

	public DecisionNode postPruning(ArrayList<ArrayList<Integer>> testSet,DecisionNode node, int l,int k,ArrayList<Integer> classAttribute,HashMap<String, Integer> attributes)
	{
		//This method is used to get a pruned tree.
		DecisionNode bestTree = node; 
		for(int i=1;i<=l;i++) {
			DecisionNode tempNode=new DecisionNode(bestTree.getId());
			tempNode=copyTree(bestTree, tempNode);
			
			DecisionNode tempTree = tempNode;
			Random rand=new Random();
			int m=rand.nextInt(k)+1;

			for(int j=1;j<=m;j++) {
				nonLeafNodes=new HashMap<>();
				nonLeafCount=0;
				int n=listOfNonLeafNodes(tempTree,0);
				
				if(n==0)
					break;
				int p=rand.nextInt(n)+1;
				
				
				DecisionNode newNode=nonLeafNodes.get(p);
				
				HashMap<Integer, Double> valueMap=gettingLeafvalues(newNode);
				
				Double max=0.0;
				Integer majority=-1;
				for(Integer in:valueMap.keySet()) {
					
					if(valueMap.get(in)>max) {
						max=valueMap.get(in);
						majority=in;

					}
				}

				newNode.setId("Class");

				newNode.setValue(majority);
				HashMap<Integer,DecisionNode> childNodes=newNode.getChildNodes();
				childNodes.clear();
				newNode.setChildNodes(childNodes);
				
				tempTree=settingNewNode(tempTree, newNode, p);
				
				

			}

			Boolean comp=accuracyComparator(testSet, bestTree,tempTree,  attributes, classAttribute);
			if(comp) {
				bestTree=tempTree;
			}


		}
		

		return bestTree;

	}
	
	private DecisionNode copyTree(DecisionNode node1,DecisionNode node2) {
		
		node2.setId(node1.getId());
		node2.setNoId(node1.getNoId());
		
		node2.setValue(node1.getValue());
		HashMap<Integer,DecisionNode> childNodes=node1.getChildNodes();
		if(childNodes==null || childNodes.isEmpty()) {
			return node2;
		}
		HashMap<Integer,DecisionNode> copyChildNodes=new HashMap<>();
		for(Integer in:childNodes.keySet()) {
			DecisionNode node=new DecisionNode(childNodes.get(in).getId());
			node=copyTree(childNodes.get(in),node);
			copyChildNodes.put(in, node);
		}
		node2.setChildNodes(copyChildNodes);
		return node2;
		
	}

	private DecisionNode settingNewNode(DecisionNode rootNode,DecisionNode newNode,int p) {
		//This method is used to set a pruned node to its parent.
		HashMap<Integer,DecisionNode> childNodes=rootNode.getChildNodes();
		
		if((childNodes==null || childNodes.isEmpty()) &&rootNode.getNoId()!=null  && !rootNode.getNoId().equals(p)) {
			return null;
		}
		
		if((childNodes==null || childNodes.isEmpty()) &&rootNode.getNoId()!=null && rootNode.getNoId().equals(p)) {
			return rootNode;
		}
		
		

		for(Integer in:childNodes.keySet()) {
			if(childNodes.get(in).getNoId()!=null && childNodes.get(in).getNoId().equals(p)) {
				childNodes.put(in, newNode);
				
				rootNode.setChildNodes(childNodes);
				return rootNode;
			}
		}

		for(Integer in:childNodes.keySet()) {
			DecisionNode node=settingNewNode(childNodes.get(in),newNode,p);
			if(node!=null) {
				childNodes.put(in, node);
			}
			
		}
		rootNode.setChildNodes(childNodes);
		return rootNode;

	}
	
	
	

	private boolean accuracyComparator(ArrayList<ArrayList<Integer>> testSet,DecisionNode node,DecisionNode tempNode,HashMap<String, Integer> attributes,ArrayList<Integer> classAttribute) {
		//This is used to compare accuracy of 2 trees.
		Integer res1=0,res2=0,res;
		for(Integer i=0;i<testSet.size();i++) {
			res=accuracyCheck(testSet.get(i), node, attributes);
			
			if(res.equals(classAttribute.get(i))) {
			
				res1++;
			}
			res=accuracyCheck(testSet.get(i), tempNode, attributes);
			
			if(res.equals(classAttribute.get(i))) {
				
				res2++;
			}

		}

		

		if(res1<res2) {
			
			return true;


		}
		else {
			
			return false;

		}










	}

	private HashMap<Integer, Double> gettingLeafvalues(DecisionNode node){
		//This method gets all the leaf values under one node
		HashMap<Integer, Double> valueMap=new HashMap<>();
		HashMap<Integer, DecisionNode> childNodes=new HashMap<>();

		HashMap<Integer, Double> tempMap=new HashMap<>();
		
		if(node.getValue()!=-1) {
			valueMap.put(node.getValue(), 1.0);
			return valueMap;
		}
		childNodes=node.getChildNodes();
		
		for(Integer in:childNodes.keySet()) {
			tempMap=gettingLeafvalues(childNodes.get(in));
			
			for(Integer in2:tempMap.keySet()) {
				
				if(valueMap.containsKey(in2)) {
					Double count=valueMap.get(in2)+tempMap.get(in2);
					valueMap.put(in2, count);

				}
				else {
					valueMap.put(in2, tempMap.get(in2));				}
			}

		}



		return valueMap;



	}

	public int listOfNonLeafNodes(DecisionNode node,int p){
		//this value put all the non leaf nodes of a tree under a map
		if(node.getValue()!=null && node.getValue()!=-1) {
			return 0;
		}
		nonLeafCount++;
		node.setNoId(nonLeafCount);
		nonLeafNodes.put(nonLeafCount, node);
		HashMap<Integer, DecisionNode> childNodes=node.getChildNodes();
		for(Integer in:childNodes.keySet()) {
			
			if(in==0) {
				
				listOfNonLeafNodes(childNodes.get(in),p);
			}
			if(in==1) {
				listOfNonLeafNodes(childNodes.get(in),p);
			}


		}




		return nonLeafCount;

	}
	public Integer accuracyCheck (ArrayList<Integer> testSet,DecisionNode node,HashMap<String,Integer> attributes) {


		while (node != null) {
			
			if (node.getValue()!=-1) {
				
				return node.getValue();
			}

			
			else {
				int index=0;
				for(String str:attributes.keySet()) {
					if(str.equals(node.getId()))
						index=attributes.get(str);

				}
				Integer value = testSet.get(index);
				
				node = node.gettingTreebyValue(value);
			}
		}

		return -1;
	}

}
