package it.sesalab.brunelleschi.graph_detection.cyclic_dependency;

import it.sesalab.brunelleschi.core.entities.ComponentType;
import it.sesalab.brunelleschi.core.entities.Component;
import it.sesalab.brunelleschi.graph_detection.DependencyGraph;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class FakeClassDependencyGraph extends DependencyGraph {

    private Component e1;
    private Component e2;

    public FakeClassDependencyGraph(boolean isPackageGraph) {
        super(isPackageGraph);
        e1 = new Component("package.A", ComponentType.CLASS);
        e2 = new Component("package.B", ComponentType.CLASS);
    }

    public Set<Component> getComponents(){
        return Set.of(e1, e2);
    }

    @Override
    public Set<Set<Component>> getCycles() {
        return Set.of(getComponents());
    }

    @Override
    public int nOfVertices() {
        return 2;
    }

    @Override
    public int nOfEdges() {
        return 2;
    }

    @Override
    public Map<Component, Integer> abstractionsDependenciesMap() {
        return Collections.emptyMap();
    }
}
