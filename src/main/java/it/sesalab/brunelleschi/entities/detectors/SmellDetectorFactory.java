package it.sesalab.brunelleschi.entities.detectors;

import it.sesalab.brunelleschi.entities.detectors.SmellDetector;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the base for a mix of Template Method
 * and Abstract Factory patterns.
 * To have a flexible number of detector the public method
 * returns a list of detector (a family).
 * The concrete detector are created by the protected methods
 * in the concrete factories.
 */
public abstract class SmellDetectorFactory {

    public List<? extends SmellDetector> makeSmellDetectors(){
        List<SmellDetector> result = new ArrayList<>();
        result.add(makeCyclicDependencyDetector());
        result.add(makeHubLikeDependencyDetector());
        result.add(makeUnstableDependencyDetector());
        result.add(makeUnutilizedAbstractionDetector());
        result.add(makeAbstractionWoDecouplingDetector());
        return result;
    }

    protected abstract SmellDetector makeAbstractionWoDecouplingDetector();

    protected abstract SmellDetector makeUnutilizedAbstractionDetector();

    protected abstract SmellDetector makeUnstableDependencyDetector();

    protected abstract SmellDetector makeHubLikeDependencyDetector();

    protected abstract SmellDetector makeCyclicDependencyDetector();


}
