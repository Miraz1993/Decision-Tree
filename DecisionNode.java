package DecisionTree;

import java.util.HashMap;

public class DecisionNode {
	
	private String id;
	private Integer value;
	private HashMap<Integer, DecisionNode> childNodes=new HashMap<>();
	private Integer noId;

	public Integer getNoId() {
		return noId;
	}


	public void setNoId(Integer noId) {
		this.noId = noId;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public Integer getValue() {
		return value;
	}


	public void setValue(Integer value) {
		this.value = value;
	}


	public HashMap<Integer, DecisionNode> getChildNodes() {
		return childNodes;
	}


	public void setChildNodes(HashMap<Integer, DecisionNode> childNodes) {
		this.childNodes = childNodes;
	}


	public DecisionNode(String id) {
		// TODO Auto-generated constructor stub
		this.id=id;
		childNodes=new HashMap<>();
		value=-1;
		
	}
	
	public DecisionNode(String id,Integer value) {
		// TODO Auto-generated constructor stub
		this.id=id;
		childNodes=new HashMap<>();
		this.value=value;
		
	}
	
	public DecisionNode gettingTreebyValue(int value) {
		HashMap<Integer, DecisionNode> childNodes = this.getChildNodes();
		for(Integer in:childNodes.keySet()) {
			if(in.equals(value)) {
				return childNodes.get(in);
			}
		}
		
		return null;
		
	}
	
	

}
