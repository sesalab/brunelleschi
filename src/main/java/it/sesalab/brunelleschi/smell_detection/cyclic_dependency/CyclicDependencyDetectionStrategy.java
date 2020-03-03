package it.sesalab.brunelleschi.smell_detection.cyclic_dependency;

import java.util.Collection;

public interface CyclicDependencyDetectionStrategy {
    Collection<CyclicDependencySmell> getCycles();
}
