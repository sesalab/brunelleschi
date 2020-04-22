package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.ArchitecturalSmell;
import it.sesalab.brunelleschi.core.entities.detector.SmellDetector;
import it.sesalab.brunelleschi.core.entities.SmellType;
import it.sesalab.brunelleschi.core.entities.Component;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


public class CyclicDependencyDetector extends GraphBasedDetector {

    public CyclicDependencyDetector(SmellDetector baseDetector, DependencyGraph projectGraph) {
        super(baseDetector, projectGraph);
    }

    @Override
    public List<ArchitecturalSmell> detectSmells() {
        List<ArchitecturalSmell> result = baseDetector.detectSmells();
        for (Set<Component> cycle : this.projectGraph.getCycles()){
            ArchitecturalSmell smell = new ArchitecturalSmell(SmellType.CYCLIC_DEPENDENCY);
            smell.addAffectedComponents(cycle);
            result.add(smell);
        }
        return result;
    }
}
