package it.sesalab.brunelleschi.entities.detectors;

import it.sesalab.brunelleschi.entities.ArchitecturalSmell;

import java.util.Collections;
import java.util.List;

/**
 * Class that represents a placeholder for detector not yet implemented
 */
public class NullSmellDetector implements SmellDetector {
    @Override
    public List<? extends ArchitecturalSmell> detectSmells() {
        return Collections.emptyList();
    }
}
