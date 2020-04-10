package it.sesalab.brunelleschi.graph_detection.jgrapht;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiModifier;
import it.sesalab.brunelleschi.core.ComponentType;
import it.sesalab.brunelleschi.core.SwComponent;
import it.sesalab.brunelleschi.graph_detection.DependencyGraph;
import lombok.Getter;
import org.jgrapht.Graph;
import org.jgrapht.alg.cycle.DirectedSimpleCycles;
import org.jgrapht.alg.cycle.TarjanSimpleCycles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class JGraphTPsiClassDependencyGraph extends DependencyGraph {

    private final Graph<PsiClass,LabeledEdge> projectGraph;

    public JGraphTPsiClassDependencyGraph(boolean isPackageGraph, Graph<PsiClass, LabeledEdge> projectGraph) {
        super(isPackageGraph);
        this.projectGraph = projectGraph;
    }

    @Override
    public Set<Set<SwComponent>> getCycles() {
        DirectedSimpleCycles<PsiClass,LabeledEdge> detector = new TarjanSimpleCycles<>(this.projectGraph);
        List<List<PsiClass>> cycles = detector.findSimpleCycles();
       return cycles.stream().map((cycle) -> cycle
               .stream()
               .map(aClass -> new SwComponent(aClass.getQualifiedName(), ComponentType.CLASS))
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
        for(PsiClass vertex: this.projectGraph.vertexSet()){
            if(vertex.isInterface() || vertex.hasModifierProperty(PsiModifier.ABSTRACT)){
                SwComponent component = new SwComponent(vertex.getQualifiedName(),ComponentType.CLASS);
                result.put(component, this.projectGraph.degreeOf(vertex));
            }
        }
        return result;
    }
}
