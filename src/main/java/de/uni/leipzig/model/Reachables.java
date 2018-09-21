package de.uni.leipzig.model;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Sets;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PROTECTED, onConstructor = @__(@VisibleForTesting))
public class Reachables {

    Set<Node> r;

    Set<Node> q;

    Set<Node> rq;

    public Reachables(Entry<EquivalenceClass, Neighbourhood> entry,
            Map<EquivalenceClass, Neighbourhood> allN) {
        this.r = Sets.union(entry.getValue().getN1(), entry.getValue().getN2()).immutableCopy();
        this.q = calculateQ(entry, allN);
        this.rq = Sets.union(r, q).immutableCopy();
    }

    private Set<Node> calculateQ(Entry<EquivalenceClass, Neighbourhood> alpha,
            Map<EquivalenceClass, Neighbourhood> allN) {
        Set<Node> q = new HashSet<>();

        for (Entry<EquivalenceClass, Neighbourhood> beta : allN.entrySet()) {

            // also necessary for equal ÄK

            if (inNeighboursAreEqual(alpha, beta)
                    && neighboursOfAlphaAreSupersetOfBeta(alpha, beta)) {
                q.addAll(beta.getKey().getNodes());
            }

        }

        return q;
    }

    @VisibleForTesting
    protected static boolean neighboursOfAlphaAreSupersetOfBeta(
            Entry<EquivalenceClass, Neighbourhood> alpha,
            Entry<EquivalenceClass, Neighbourhood> beta) {
        return alpha.getValue().getN1().containsAll(beta.getValue().getN1());
    }

    @VisibleForTesting
    protected static boolean inNeighboursAreEqual(Entry<EquivalenceClass, Neighbourhood> alpha,
            Entry<EquivalenceClass, Neighbourhood> beta) {
        Set<Node> nInAlpha = alpha.getValue().getNIn();
        Set<Node> nInBeta = beta.getValue().getNIn();

        return nInBeta.containsAll(nInAlpha)
                && nInAlpha.containsAll(nInBeta);
    }
}
