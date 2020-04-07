package it.sesalab.brunelleschi.entities.detectors;

import it.sesalab.brunelleschi.entities.ArchitecturalSmell;

import java.util.List;

public interface SmellDetector {
    List<? extends ArchitecturalSmell> detectSmells();
}
