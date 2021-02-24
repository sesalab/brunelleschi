package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.ComponentType;
import it.sesalab.brunelleschi.core.entities.Component;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public class FakeClassGraph extends DependencyGraph {

    private Component e1;
    private Component e2;

    public FakeClassGraph() {
        super(false);
        e1 = new Component("package.A", ComponentType.CLASS, true);
        e2 = new Component("package.B", ComponentType.CLASS, true);
    }

    public List<Component> getComponents(){
        return List.of(e1, e2);
    }

    @Override
    public List<List<Component>> getElementsInStronglyConnectedComponents() {
        return List.of(getComponents());
    }

    @Override
    public List<DependencyGraph> getStronglyConnectedComponents() {
        return null;
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
        return Set.of(new DependencyDescriptor(e1,1,1), new DependencyDescriptor(e2,1,1));
    }

    @Override
    public Collection<Component> getUnstableComponents() {
        return List.of(e1);
    }
}
