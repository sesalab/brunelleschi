package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.entities.ArchitecturalSmell;
import it.sesalab.brunelleschi.entities.SmellDetector;
import it.sesalab.brunelleschi.entities.SmellType;
import it.sesalab.brunelleschi.entities.SwComponent;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class CycleDependencyDetector implements SmellDetector {

    private final DependencyGraph dependencyGraph;

    @Override
    public List<? extends ArchitecturalSmell> detectSmells() {
        List<ArchitecturalSmell> result = new ArrayList<>();
        for( List<SwComponent> cycle : dependencyGraph.getCycles()){
            ArchitecturalSmell smell = new ArchitecturalSmell(SmellType.CYCLIC_DEPENDENCY);
            smell.addAffectedComponents(cycle);
            result.add(smell);
        }
        return result;
    }
}
