package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.Component;
import it.sesalab.brunelleschi.core.entities.ComponentType;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class FakePackageGraph extends DependencyGraph {

    public FakePackageGraph() {
        super(true);
    }

    @Override
    public Set<Set<Component>> getCycles() {
        return null;
    }

    @Override
    public int nOfVertices() {
        return 0;
    }

    @Override
    public int nOfEdges() {
        return 0;
    }

    @Override
    public Collection<DependencyDescriptor> evaluateDependencies() {
        return Set.of(new DependencyDescriptor(new Component("unstableDependency", ComponentType.PACKAGE),2,3));
    }

    @Override
    public Collection<Component> getUnstableComponents() {
        return List.of(new Component("unstableDependency", ComponentType.PACKAGE));
    }
}
