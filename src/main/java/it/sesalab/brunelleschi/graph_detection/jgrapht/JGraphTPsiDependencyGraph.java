package it.sesalab.brunelleschi.graph_detection.jgrapht;

import com.intellij.psi.*;
import it.sesalab.brunelleschi.core.entities.ComponentType;
import it.sesalab.brunelleschi.core.entities.SwComponent;
import it.sesalab.brunelleschi.graph_detection.DependencyGraph;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.Graph;
import org.jgrapht.alg.cycle.DirectedSimpleCycles;
import org.jgrapht.alg.cycle.TarjanSimpleCycles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class JGraphTPsiDependencyGraph<T extends PsiQualifiedNamedElement & PsiModifierListOwner> extends DependencyGraph {

    private final Graph<T,LabeledEdge> projectGraph;

    public JGraphTPsiDependencyGraph(boolean isPackageGraph, Graph<T, LabeledEdge> projectGraph) {
        super(isPackageGraph);
        this.projectGraph = projectGraph;
    }

    @Override
    public Set<Set<SwComponent>> getCycles() {
        DirectedSimpleCycles<T,LabeledEdge> detector = new TarjanSimpleCycles<>(this.projectGraph);
        List<List<T>> cycles = detector.findSimpleCycles();
       return cycles.stream().map((cycle) -> cycle
               .stream()
               .map(qualifiedNameOwner ->
                       new SwComponent(qualifiedNameOwner.getQualifiedName(), componentType()))
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
    public Map<SwComponent, Integer> abstractionsDependenciesMap() {
        Map<SwComponent, Integer> result = new HashMap<>();
        for(T vertex: this.projectGraph.vertexSet()){
            if(vertex.hasModifierProperty(PsiModifier.ABSTRACT)){
                SwComponent component = new SwComponent(vertex.getQualifiedName(),componentType());
                result.put(component, this.projectGraph.degreeOf(vertex));
            }
        }
        return result;
    }

    @NotNull
    private ComponentType componentType() {
        return isPackageGraph? ComponentType.PACKAGE :ComponentType.CLASS;
    }
}
