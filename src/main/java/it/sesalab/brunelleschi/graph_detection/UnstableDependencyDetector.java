package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.ArchitecturalSmell;
import it.sesalab.brunelleschi.core.entities.SmellType;
import it.sesalab.brunelleschi.core.entities.detector.SmellDetector;

import java.util.ArrayList;
import java.util.List;

public class UnstableDependencyDetector extends GraphBasedDetector {

    private final double smellThreshold;

    public UnstableDependencyDetector(SmellDetector baseDetector, DependencyGraph projectGraph, double smellThreshold) {
        super(baseDetector, projectGraph);
        this.smellThreshold = smellThreshold;
    }

    @Override
    public List<ArchitecturalSmell> detectSmells() {
        List<ArchitecturalSmell> result = new ArrayList<>();

        for(DependencyDescriptor descriptor: projectGraph.evaluateDependencies()){
            if(descriptor.getInstability() >= smellThreshold){
                ArchitecturalSmell detectedSmell = new ArchitecturalSmell(SmellType.UNSTABLE_DEPENDENCY);
                detectedSmell.addAffectedComponent(descriptor.getComponent());
                result.add(detectedSmell);
            }
        }

        return result;
    }
}
