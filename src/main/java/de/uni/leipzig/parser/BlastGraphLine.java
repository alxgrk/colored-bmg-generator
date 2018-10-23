package de.uni.leipzig.parser;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.primitives.Chars;

import de.uni.leipzig.model.Node;
import lombok.*;

@Value
public class BlastGraphLine {

    private Gene gene1;

    private Gene gene2;

    private Double evalueAB;

    private Double evalueBA;

    private Integer bitscoreAB;

    private Integer bitscoreBA;

    // E_10 C_10 1.21e-143 425 4.59e-136 405

    public BlastGraphLine(String line, Function<String, String> firstConversion,
            Function<String, String> secondConversion) {
        String[] splitted = line.split("\t");

        gene1 = new Gene(splitted[0], firstConversion);
        gene2 = new Gene(splitted[1], secondConversion);

        evalueAB = Double.valueOf(splitted[2]);
        evalueBA = Double.valueOf(splitted[4]);

        bitscoreAB = Integer.parseInt(splitted[3]);
        bitscoreBA = Integer.parseInt(splitted[5]);
    }

    @ToString
    class Gene {

        @Getter
        private String label;

        @Getter
        private String name;

        private Node node;

        public Gene(String gene, Function<String, String> labelConversion) {
            String[] split = gene.split("_");
            label = labelConversion.apply(split[0]);
            name = split[1];

            List<Integer> id = Chars.asList(name.toCharArray())
                    .stream()
                    .map(c -> Integer.parseInt(c.toString()))
                    .collect(Collectors.toList());
            node = Node.of(label, id);
        }

        public Node asNode() {
            return node;
        }

    }
}
