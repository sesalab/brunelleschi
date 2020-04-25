package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public abstract class DependencyGraph {

    protected final boolean isPackageGraph;

    public DependencyGraph(boolean isPackageGraph) {
        this.isPackageGraph = isPackageGraph;
    }

    public boolean isPackageGraph() {
        return isPackageGraph;
    }

    public abstract Set<Set<Component>> getCycles();

    public abstract int nOfVertices();

    public abstract int nOfEdges();

    //TODO: Fix HubLikeFormula
    public abstract Collection<DependencyDescriptor> evaluateDependencies();
}
