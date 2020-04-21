package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.*;
import lombok.RequiredArgsConstructor;

import java.util.*;

@RequiredArgsConstructor
public class HubLikeDependencyDetector implements SmellDetector {

    private final DependencyGraph dependencyGraph;
    private final Integer threshold;

    @Override
    public List<ArchitecturalSmell> detectSmells() throws NotAllowedDetection {
        if(dependencyGraph.isPackageGraph()){
            throw new NotAllowedDetection("Hub-Like Dependency detection not allowed on package graph");
        }
        List<ArchitecturalSmell> result = new LinkedList<>();
        Map<SwComponent, Integer> abstractionsDependenciesMap = dependencyGraph.abstractionsDependenciesMap();
        for (SwComponent abstraction: abstractionsDependenciesMap.keySet()){
            if(abstractionsDependenciesMap.get(abstraction) >= threshold){
                ArchitecturalSmell detectdedSmell = new ArchitecturalSmell(SmellType.HUB_LIKE_DEPENDENCY);
                detectdedSmell.addAffectedComponent(abstraction);
                result.add(detectdedSmell);
            }
        }
        return result;
    }
}
