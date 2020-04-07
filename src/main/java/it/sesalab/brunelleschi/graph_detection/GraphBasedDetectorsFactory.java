package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.entities.detectors.NullSmellDetector;
import it.sesalab.brunelleschi.entities.detectors.SmellDetector;
import it.sesalab.brunelleschi.entities.detectors.SmellDetectorFactory;
import it.sesalab.brunelleschi.graph_detection.cyclic_dependency.CyclicDependencyDetector;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class GraphBasedDetectorsFactory extends SmellDetectorFactory {

    private final DependencyGraph dependencyGraph;

    @Override
    protected SmellDetector makeAbstractionWoDecouplingDetector() {
        return new NullSmellDetector();
    }

    @Override
    protected SmellDetector makeUnutilizedAbstractionDetector() {
        return new NullSmellDetector();
    }

    @Override
    protected SmellDetector makeUnstableDependencyDetector() {
        return new NullSmellDetector();
    }

    @Override
    protected SmellDetector makeHubLikeDependencyDetector() {
        return new NullSmellDetector();
    }

    @Override
    protected SmellDetector makeCyclicDependencyDetector() {
        return new CyclicDependencyDetector(this.dependencyGraph);
    }
}
