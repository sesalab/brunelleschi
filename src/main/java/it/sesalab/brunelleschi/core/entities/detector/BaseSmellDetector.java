package it.sesalab.brunelleschi.core.entities.detector;

import it.sesalab.brunelleschi.core.entities.ArchitecturalSmell;

import java.util.ArrayList;
import java.util.List;

public class BaseSmellDetector implements SmellDetector {
    @Override
    public List<ArchitecturalSmell> detectSmells() throws NotAllowedDetection {
        return new ArrayList<>();
    }
}
