package it.sesalab.brunelleschi.graph_detection.jgrapht;

import com.intellij.psi.*;
import it.sesalab.brunelleschi.core.entities.ComponentType;
import it.sesalab.brunelleschi.core.entities.Component;
import it.sesalab.brunelleschi.graph_detection.DependencyDescriptor;
import it.sesalab.brunelleschi.graph_detection.DependencyGraph;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.Graph;
import org.jgrapht.alg.cycle.DirectedSimpleCycles;
import org.jgrapht.alg.cycle.TarjanSimpleCycles;

import java.util.*;
import java.util.stream.Collectors;

@Getter
public class JGraphTPsiDependencyGraph<T extends PsiQualifiedNamedElement & PsiModifierListOwner> extends DependencyGraph {

    private final Graph<T,LabeledEdge> projectGraph;

    public JGraphTPsiDependencyGraph(boolean isPackageGraph, Graph<T, LabeledEdge> projectGraph) {
        super(isPackageGraph);
        this.projectGraph = projectGraph;
    }

    @Override
    public Set<Set<Component>> getCycles() {
        DirectedSimpleCycles<T,LabeledEdge> detector = new TarjanSimpleCycles<>(this.projectGraph);
        List<List<T>> cycles = detector.findSimpleCycles();
       return cycles.stream().map((cycle) -> cycle
               .stream()
               .map(qualifiedNameOwner ->
                       new Component(qualifiedNameOwner.getQualifiedName(), componentType()))
               .collect(Collectors.toSet())).collect(Collectors.toSet());
    }

    @Override
    public int nOfVertices() {
        return this.projectGraph.vertexSet().size();
    }

    @Override
    public int nOfEdges() {
        return this.projectGraph.edgeSet().size();
    }

    @Override
    public Collection<DependencyDescriptor> evaluateDependencies() {
        Set<DependencyDescriptor> result = new HashSet<>();
        for(T vertex: projectGraph.vertexSet()){
            DependencyDescriptor descriptor = composeDependencyDescriptor(vertex);
            result.add(descriptor);
        }
        return result;
    }

    private boolean isAbstraction(T vertex) {
        return vertex.hasModifierProperty(PsiModifier.ABSTRACT);
    }

    @NotNull
    private DependencyDescriptor composeDependencyDescriptor(T vertex) {
        Component abstraction = new Component(vertex.getQualifiedName(),componentType(), isAbstraction(vertex));
        int fanIn = projectGraph.inDegreeOf(vertex);
        int fanOut = projectGraph.outDegreeOf(vertex);
        return new DependencyDescriptor(abstraction, fanIn, fanOut);
    }

    @NotNull
    private ComponentType componentType() {
        return isPackageGraph? ComponentType.PACKAGE :ComponentType.CLASS;
    }
}
