package droolshelloworld;

import java.util.ArrayList;
import java.util.List;



public class RoolVO {
	
	public RoolVO() {
	listValue = new ArrayList<String>(0);	
	}
	private String  stringValue;
	private boolean booleanValue;
	private List<String> listValue;
	
	
	public String getStringValue() {
		return stringValue;
	}
	public void setStringValue(String stringValue) {
		this.stringValue = stringValue;
	}
	public boolean isBooleanValue() {
		return booleanValue;
	}
	public void setBooleanValue(boolean booleanValue) {
		this.booleanValue = booleanValue;
	}
	public List<String> getListValue() {
		return listValue;
	}
	public void setListValue(List<String> listValue) {
		this.listValue = listValue;
	}
	
	
	
	
}

