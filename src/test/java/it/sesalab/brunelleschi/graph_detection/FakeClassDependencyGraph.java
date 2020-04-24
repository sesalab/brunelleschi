package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.ComponentType;
import it.sesalab.brunelleschi.core.entities.Component;

import java.util.Collection;
import java.util.Collections;
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
    public Collection<DependencyDescriptor> getAbstractionsWithDependencies() {
        return Set.of(new DependencyDescriptor(e1,1,0), new DependencyDescriptor(e2,0,1));
    }
}
