package it.sesalab.brunelleschi.smell_detection;

import lombok.Builder;
import lombok.Singular;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Builder
public class SmellDetectorFacade {

    @Singular
    protected Set<SmellDetector> classLevelDetectors;

    Collection<ArchitecturalSmell> getClassLevelSmells(){
        List<ArchitecturalSmell> smells = new LinkedList<>();
        for (SmellDetector detector: classLevelDetectors){
            smells.addAll(detector.getSmells());
        }
        return smells;
    }

}
