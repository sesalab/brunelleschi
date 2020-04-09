package it.sesalab.brunelleschi.graph_detection.cyclic_dependency;

import it.sesalab.brunelleschi.entities.ArchitecturalSmell;
import it.sesalab.brunelleschi.entities.detectors.SmellDetector;
import it.sesalab.brunelleschi.entities.SmellType;
import it.sesalab.brunelleschi.entities.SwComponent;
import it.sesalab.brunelleschi.graph_detection.DependencyGraph;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class CyclicDependencyDetector implements SmellDetector {

    private final DependencyGraph dependencyGraph;

    @Override
    public List<? extends ArchitecturalSmell> detectSmells() {
        List<ArchitecturalSmell> result = new ArrayList<>();
        for (Set<SwComponent> cycle : dependencyGraph.getCycles()){
            ArchitecturalSmell smell = new ArchitecturalSmell(SmellType.CYCLIC_DEPENDENCY);
            smell.addAffectedComponents(cycle);
            result.add(smell);
        }
        return result;
    }
}
