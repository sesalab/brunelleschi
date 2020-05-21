package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.detector.BaseSmellDetector;
import it.sesalab.brunelleschi.core.entities.detector.SmellDetector;
import it.sesalab.brunelleschi.core.entities.detector.SmellDetectorBuilder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GraphBasedDetectorBuilder implements SmellDetectorBuilder {

    private SmellDetector instanceToBuild = new BaseSmellDetector();
    private final DependencyGraph packageGraph;
    private final DependencyGraph classGraph;


    @Override
    public SmellDetectorBuilder enableCyclicDependencyDetection() {
        instanceToBuild = new CyclicDependencyDetector(instanceToBuild, packageGraph);
        instanceToBuild = new CyclicDependencyDetector(instanceToBuild, classGraph);
        return this;
    }

    @Override
    public SmellDetectorBuilder enableHubLikeDependencyDetection(int threshold) {
        instanceToBuild = new HubLikeDependencyDetector(instanceToBuild, classGraph, threshold);
        return this;
    }

    @Override
    public SmellDetectorBuilder enableUnstableDependencyDetection() {
        instanceToBuild = new UnstableDependencyDetector(instanceToBuild, packageGraph);
        return this;
    }

    @Override
    public SmellDetector build() {
        return instanceToBuild;
    }
}
