package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.SwComponent;

import java.util.Map;
import java.util.Set;

public abstract class DependencyGraph {

    protected final boolean isPackageGraph;

    public DependencyGraph(boolean isPackageGraph) {
        this.isPackageGraph = isPackageGraph;
    }

    public boolean isPackageGraph() {
        return isPackageGraph;
    }

    public abstract Set<Set<SwComponent>> getCycles();

    public abstract int nOfVertices();

    public abstract int nOfEdges();

    public abstract Map<SwComponent, Integer> abstractionsDependenciesMap();

}
