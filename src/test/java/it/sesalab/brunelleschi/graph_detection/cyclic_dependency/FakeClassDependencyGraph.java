package it.sesalab.brunelleschi.graph_detection.cyclic_dependency;

import it.sesalab.brunelleschi.core.ComponentType;
import it.sesalab.brunelleschi.core.SwComponent;
import it.sesalab.brunelleschi.graph_detection.DependencyGraph;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class FakeClassDependencyGraph extends DependencyGraph {

    private SwComponent e1;
    private SwComponent e2;

    public FakeClassDependencyGraph(boolean isPackageGraph) {
        super(isPackageGraph);
        e1 = new SwComponent("package.A", ComponentType.CLASS);
        e2 = new SwComponent("package.B", ComponentType.CLASS);
    }

    public Set<SwComponent> getComponents(){
        return Set.of(e1, e2);
    }

    @Override
    public Set<Set<SwComponent>> getCycles() {
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
    public Map<SwComponent, Integer> abstractionsDependenciesMap() {
        return Collections.emptyMap();
    }
}
