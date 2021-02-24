package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.*;
import it.sesalab.brunelleschi.core.entities.detector.NotAllowedDetection;
import it.sesalab.brunelleschi.core.entities.detector.SmellDetector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * Implements DESIGNITE-Like algorithm. Fan-in and fan-out higher
 * than a given threshold.
 */
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
            ArchitecturalSmell detectedSmell = null;
            if(isBorderlineHublike(descriptor)){
                detectedSmell = makeHubLikeFrom(descriptor.getComponent(), SmellType.BORDERLINE_HUB_LIKE);
                System.out.println("HBLK: "+descriptor.getComponent().getQualifiedName()+" FI:"+descriptor.getFanIn()+" FO:"+descriptor.getFanOut());
            }
            if(hasHubLikeModularization(descriptor)) {
                detectedSmell = makeHubLikeFrom(descriptor.getComponent(), SmellType.HUB_LIKE_DEPENDENCY);
            }
            if(null != detectedSmell){
                result.add(detectedSmell);
            }
        }
        return result;
    }

    private boolean hasHubLikeModularization(DependencyDescriptor descriptor) {
        return descriptor.getFanIn() >= fanInThreshold && descriptor.getFanOut() >= fanOutThreshold;
    }

    private boolean isBorderlineHublike(DependencyDescriptor descriptor) {
        return descriptor.getFanIn() >= (fanInThreshold/2) && descriptor.getFanOut() >= (fanOutThreshold/2);
    }

    @NotNull
    private ArchitecturalSmell makeHubLikeFrom(Component abstraction, SmellType type) {
        ArchitecturalSmell detectedSmell = new ArchitecturalSmell(type);
        detectedSmell.addAffectedComponent(abstraction);
        return detectedSmell;
    }

}
