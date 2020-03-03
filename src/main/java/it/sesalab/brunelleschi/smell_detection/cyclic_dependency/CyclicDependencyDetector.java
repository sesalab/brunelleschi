package it.sesalab.brunelleschi.smell_detection.cyclic_dependency;

import it.sesalab.brunelleschi.smell_detection.SmellDetector;

import java.util.Collection;

public class CyclicDependencyDetector extends SmellDetector {

    private CyclicDependencyDetectionStrategy strategy;

    @Override
    public Collection<CyclicDependencySmell> getSmells() {
        return strategy.getCycles();
    }
}
