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

    public Reachables(Entry<ThinnessClass, Neighbourhood> entry,
            Map<ThinnessClass, Neighbourhood> allN) {
        this.r = Sets.union(entry.getValue().getN1(), entry.getValue().getN2()).immutableCopy();
        this.q = calculateQ(entry, allN);
        this.rq = Sets.union(r, q).immutableCopy();
    }

    private Set<Node> calculateQ(Entry<ThinnessClass, Neighbourhood> alpha,
            Map<ThinnessClass, Neighbourhood> allN) {
        Set<Node> q = new HashSet<>();

        for (Entry<ThinnessClass, Neighbourhood> beta : allN.entrySet()) {

            // also necessary for equal TC

            if (inNeighboursAreEqual(alpha, beta)
                    && neighboursOfAlphaAreSupersetOfBeta(alpha, beta)) {
                q.addAll(beta.getKey().getNodes());
            }

        }

        return q;
    }

    @VisibleForTesting
    protected static boolean neighboursOfAlphaAreSupersetOfBeta(
            Entry<ThinnessClass, Neighbourhood> alpha,
            Entry<ThinnessClass, Neighbourhood> beta) {
        return alpha.getValue().getN1().containsAll(beta.getValue().getN1());
    }

    @VisibleForTesting
    protected static boolean inNeighboursAreEqual(Entry<ThinnessClass, Neighbourhood> alpha,
            Entry<ThinnessClass, Neighbourhood> beta) {
        Set<Node> nInAlpha = alpha.getValue().getNIn();
        Set<Node> nInBeta = beta.getValue().getNIn();

        return nInBeta.containsAll(nInAlpha)
                && nInAlpha.containsAll(nInBeta);
    }
}
