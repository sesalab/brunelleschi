package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.ComponentType;
import it.sesalab.brunelleschi.core.entities.Component;

import java.util.Collection;
import java.util.Set;

public class FakeClassGraph extends DependencyGraph {

    private Component e1;
    private Component e2;

    public FakeClassGraph() {
        super(false);
        e1 = new Component("package.A", ComponentType.CLASS, true);
        e2 = new Component("package.B", ComponentType.CLASS, true);
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
    public Collection<DependencyDescriptor> evaluateDependencies() {
        return Set.of(new DependencyDescriptor(e1,1,0), new DependencyDescriptor(e2,0,1));
    }
}
