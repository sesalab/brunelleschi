package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.*;
import it.sesalab.brunelleschi.core.entities.detector.NotAllowedDetection;
import it.sesalab.brunelleschi.core.entities.detector.SmellDetector;
import org.jetbrains.annotations.NotNull;

import java.util.*;


public class HubLikeDependencyDetector extends GraphBasedDetector {

    private final Integer smellinessThreshold;
    private Map<Component, Integer> abstractionsWithDependencies;

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
        abstractionsWithDependencies = projectGraph.abstractionsDependenciesMap();

        for (Component abstraction: getAbstractions()){
            if(numberOfDependenciesOf(abstraction) >= smellinessThreshold){
                ArchitecturalSmell detectedSmell = makeHubLikeFrom(abstraction);
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

    private Integer numberOfDependenciesOf(Component abstraction) {
        return abstractionsWithDependencies.get(abstraction);
    }

    @NotNull
    private Set<Component> getAbstractions() {
        return abstractionsWithDependencies.keySet();
    }
}
