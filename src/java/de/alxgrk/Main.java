package de.alxgrk;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import de.alxgrk.model.Tree;

public class Main {

    public static void main(String[] args) throws IOException {

        FileReader reader = new FileReader(new File(args[0]));
        Tree result = AhoBuild.build(reader.getTripleSet(), new ArrayList<>(reader
                .getLeaveSet()));

        System.out.println(result.toString());
    }

}