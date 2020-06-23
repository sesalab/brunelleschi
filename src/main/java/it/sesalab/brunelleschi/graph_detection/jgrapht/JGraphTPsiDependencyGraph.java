package it.sesalab.brunelleschi.graph_detection.jgrapht;

import com.intellij.psi.*;
import it.sesalab.brunelleschi.core.entities.ComponentType;
import it.sesalab.brunelleschi.core.entities.Component;
import it.sesalab.brunelleschi.graph_detection.DependencyDescriptor;
import it.sesalab.brunelleschi.graph_detection.DependencyGraph;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.GabowStrongConnectivityInspector;
import org.jgrapht.alg.cycle.DirectedSimpleCycles;
import org.jgrapht.alg.cycle.TarjanSimpleCycles;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;

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
    public List<List<Component>> getStronglyConnectedComponents() {
        StrongConnectivityAlgorithm<T,LabeledEdge> connectivityAlgorithm = new GabowStrongConnectivityInspector<>(this.projectGraph);
        List<Set<T>> stronglyConnectedSets = connectivityAlgorithm.stronglyConnectedSets();
        return stronglyConnectedSets.stream().map(ts ->
                ts.stream().map(t ->
                        new Component(t.getQualifiedName(),componentType(),isAbstraction(t)))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
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

    @Override
    public Collection<Component> getUnstableComponents() {
        if(!isPackageGraph){
            throw new IllegalStateException("Method must be called on package graph");
        }
        List<Component> result = new ArrayList<>();
        for(T currentVertex: projectGraph.vertexSet()) {
            List<T> successors = Graphs.successorListOf(projectGraph, currentVertex);
            DependencyDescriptor currentVertexDescriptor = composeDependencyDescriptor(currentVertex);
            for(T successor : successors){
                DependencyDescriptor successorDescriptor = composeDependencyDescriptor(successor);
                if(currentVertexDescriptor.getInstability() <= successorDescriptor.getInstability()){
                    result.add(new Component(currentVertex.getQualifiedName(),componentType(), isAbstraction(currentVertex)));
                }
            }
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
