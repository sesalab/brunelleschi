package it.sesalab.brunelleschi.core;

import java.util.Collection;
import java.util.Set;

public abstract class SmellDetectorFactory {

    public Collection<SmellDetector> makeClassLevelDetectors(){
        return Set.of(
                cyclicDependencyDetector(false),
                hubLikeDependencyDetector(),
                abstractionWoDecouplingDetector(),
                unutilizedAbstractionDetector());
    }

    public Collection<SmellDetector> makePackageLevelDetectors(){
        return Set.of(cyclicDependencyDetector(true), unstableDependencyDetector());
    }

    protected abstract SmellDetector cyclicDependencyDetector(boolean isPackageLevel);

    protected abstract SmellDetector hubLikeDependencyDetector();

    protected abstract SmellDetector abstractionWoDecouplingDetector();

    protected abstract SmellDetector unutilizedAbstractionDetector();

    protected abstract SmellDetector unstableDependencyDetector();
}
