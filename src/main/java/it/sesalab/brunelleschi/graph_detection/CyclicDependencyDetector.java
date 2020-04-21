package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.ArchitecturalSmell;
import it.sesalab.brunelleschi.core.entities.SmellDetector;
import it.sesalab.brunelleschi.core.entities.SmellType;
import it.sesalab.brunelleschi.core.entities.SwComponent;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class CyclicDependencyDetector implements SmellDetector {

    private final DependencyGraph dependencyGraph;

    @Override
    public List<ArchitecturalSmell> detectSmells() {
        List<ArchitecturalSmell> result = new ArrayList<>();
        for (Set<SwComponent> cycle : dependencyGraph.getCycles()){
            ArchitecturalSmell smell = new ArchitecturalSmell(SmellType.CYCLIC_DEPENDENCY);
            smell.addAffectedComponents(cycle);
            result.add(smell);
        }
        return result;
    }
}
