package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.*;
import it.sesalab.brunelleschi.core.entities.detector.NotAllowedDetection;
import it.sesalab.brunelleschi.core.entities.detector.SmellDetector;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class HubLikeDependencyDetector extends GraphBasedDetector {

    private final Integer fanInThreshold;
    private final Integer fanOutThreshold;

    public HubLikeDependencyDetector(SmellDetector baseDetector, DependencyGraph projectGraph, Integer dependencyThreshold) {
        this(baseDetector,projectGraph,dependencyThreshold,dependencyThreshold);
    }

    public HubLikeDependencyDetector(SmellDetector baseDetector, DependencyGraph projectGraph, Integer fanInThreshold, Integer fanOutThreshold) {
        super(baseDetector, projectGraph);
        this.fanInThreshold = fanInThreshold;
        this.fanOutThreshold = fanOutThreshold;
    }

    @Override
    public List<ArchitecturalSmell> detectSmells(){
        if(projectGraph.isPackageGraph()){
            throw new NotAllowedDetection("Hub-Like Dependency detection not allowed on package graph");
        }
        List<ArchitecturalSmell> result = baseDetector.detectSmells();
        Collection<DependencyDescriptor> abstractionsWithDependencies = projectGraph.evaluateDependencies();

        for (DependencyDescriptor descriptor: abstractionsWithDependencies){
            if(hasHubLikeModularization(descriptor)) {
                if (descriptor.totalDependencies() >= fanInThreshold) {
                    ArchitecturalSmell detectedSmell = makeHubLikeFrom(descriptor.getComponent());
                    result.add(detectedSmell);
                }
            }
        }
        return result;
    }

    private boolean hasHubLikeModularization(DependencyDescriptor descriptor) {
        return descriptor.getFanIn() >= fanInThreshold && descriptor.getFanOut() >= fanOutThreshold;
    }

    @NotNull
    private ArchitecturalSmell makeHubLikeFrom(Component abstraction) {
        ArchitecturalSmell detectedSmell = new ArchitecturalSmell(SmellType.HUB_LIKE_DEPENDENCY);
        detectedSmell.addAffectedComponent(abstraction);
        return detectedSmell;
    }

}
