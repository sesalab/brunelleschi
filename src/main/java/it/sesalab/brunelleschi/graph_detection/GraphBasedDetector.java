package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.detector.AbstractSmellDetectorDecorator;
import it.sesalab.brunelleschi.core.entities.detector.SmellDetector;


public abstract class GraphBasedDetector extends AbstractSmellDetectorDecorator {
    protected final DependencyGraph projectGraph;

    public GraphBasedDetector(SmellDetector baseDetector, DependencyGraph projectGraph) {
        super(baseDetector);
        this.projectGraph = projectGraph;
    }
}
