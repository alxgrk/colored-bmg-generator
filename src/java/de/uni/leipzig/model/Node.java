package de.uni.leipzig.model;

import java.util.List;

import lombok.Value;
import lombok.experimental.FieldDefaults;

@Value
@FieldDefaults(makeFinal= false)
public class Node {

	Integer label;
	
    List<Integer> ids;
    
    String path = "";

    public static Node of(Integer label, List<Integer> id) {
        if (id.isEmpty())
            throw new IllegalArgumentException("Node value length must be >= 0");

        return new Node(label, id);
    }

    private Node(Integer label, List<Integer> id) {
        this.label = label;
		this.ids = id;
		for (Integer i : id){
			this.path += i;
		}
    }

    @Override
    public String toString() {
        return "(" + label + ")" + path;
    }

}
