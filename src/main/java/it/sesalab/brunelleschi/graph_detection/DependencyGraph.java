package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.Component;

import java.util.Collection;
import java.util.List;

public abstract class DependencyGraph {

    protected final boolean isPackageGraph;

    public DependencyGraph(boolean isPackageGraph) {
        this.isPackageGraph = isPackageGraph;
    }

    public boolean isPackageGraph() {
        return isPackageGraph;
    }

    public abstract List<List<Component>> getElementsInStronglyConnectedComponents();

    public abstract List<DependencyGraph> getStronglyConnectedComponents();

    public abstract int nOfVertices();

    public abstract int nOfEdges();

    public abstract Collection<DependencyDescriptor> evaluateDependencies();

    // TODO: TEST!!!!!
    public abstract Collection<Component> getUnstableComponents();
}
