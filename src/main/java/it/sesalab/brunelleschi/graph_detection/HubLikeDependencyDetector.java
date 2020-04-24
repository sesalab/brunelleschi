package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.*;
import it.sesalab.brunelleschi.core.entities.detector.NotAllowedDetection;
import it.sesalab.brunelleschi.core.entities.detector.SmellDetector;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class HubLikeDependencyDetector extends GraphBasedDetector {

    private final Integer smellinessThreshold;

    public HubLikeDependencyDetector(SmellDetector baseDetector, DependencyGraph projectGraph, Integer smellinessThreshold) {
        super(baseDetector, projectGraph);
        this.smellinessThreshold = smellinessThreshold;
    }

    @Override
    public List<ArchitecturalSmell> detectSmells(){
        if(projectGraph.isPackageGraph()){
            throw new NotAllowedDetection("Hub-Like Dependency detection not allowed on package graph");
        }
        List<ArchitecturalSmell> result = baseDetector.detectSmells();
        Collection<DependencyDescriptor> abstractionsWithDependencies = projectGraph.getAbstractionsWithDependencies();

        for (DependencyDescriptor descriptor: abstractionsWithDependencies){
            if(descriptor.getNOfDependencies() >= smellinessThreshold){
                ArchitecturalSmell detectedSmell = makeHubLikeFrom(descriptor.getComponent());
                result.add(detectedSmell);
            }
        }
        return result;
    }

    @NotNull
    private ArchitecturalSmell makeHubLikeFrom(Component abstraction) {
        ArchitecturalSmell detectedSmell = new ArchitecturalSmell(SmellType.HUB_LIKE_DEPENDENCY);
        detectedSmell.addAffectedComponent(abstraction);
        return detectedSmell;
    }

}
