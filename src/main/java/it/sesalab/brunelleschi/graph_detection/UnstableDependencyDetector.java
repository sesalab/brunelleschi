package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.ArchitecturalSmell;
import it.sesalab.brunelleschi.core.entities.Component;
import it.sesalab.brunelleschi.core.entities.SmellType;
import it.sesalab.brunelleschi.core.entities.detector.SmellDetector;

import java.util.ArrayList;
import java.util.List;

public class UnstableDependencyDetector extends GraphBasedDetector {

    public UnstableDependencyDetector(SmellDetector baseDetector, DependencyGraph projectGraph) {
        super(baseDetector, projectGraph);
    }

    @Override
    public List<ArchitecturalSmell> detectSmells() {
        List<ArchitecturalSmell> result = new ArrayList<>();

        for(Component component: projectGraph.getUnstableComponents()){
                ArchitecturalSmell detectedSmell = new ArchitecturalSmell(SmellType.UNSTABLE_DEPENDENCY);
                detectedSmell.addAffectedComponent(component);
                result.add(detectedSmell);
        }

        return result;
    }
}
