package it.sesalab.brunelleschi.core.entities.detector;

import it.sesalab.brunelleschi.core.entities.ArchitecturalSmell;

import java.util.List;

public interface SmellDetector {
    List<ArchitecturalSmell> detectSmells();
}
