package de.uni.leipzig.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Value;
import lombok.experimental.FieldDefaults;

@Value
@FieldDefaults(makeFinal= false)
public class Node {

	Integer label;
	
    List<Integer> ids;
    
    String path = "";

    public static Node helpNode() {
        return new Node(-1, new ArrayList<>());
    }
    
    public static Node of(Integer label, List<Integer> id) {
        return new Node(label, id);
    }

    private Node(Integer label, List<Integer> id) {
        this.label = label;
		this.ids = id;
		for (Integer i : id){
			this.path += i;
		}
    }

    public boolean isHelpNode() {
		return label == -1;
	}
    
    @Override
    public String toString() {
    	if (isHelpNode())
    		return "*";
    	
        return  label + "-" + path;
    }

}
