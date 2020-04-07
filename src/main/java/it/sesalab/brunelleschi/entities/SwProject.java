package it.sesalab.brunelleschi.entities;

import it.sesalab.brunelleschi.entities.detectors.SmellDetector;

import java.util.ArrayList;
import java.util.List;

public class SwProject {

    private List<SmellDetector> detectors;

    public List<ArchitecturalSmell> detectSmells(){
        List<ArchitecturalSmell> result = new ArrayList<>();
        for (SmellDetector detector : detectors){
            result.addAll(detector.detectSmells());
        }
        return result;
    }

}
