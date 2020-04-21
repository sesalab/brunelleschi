package it.sesalab.brunelleschi.core.entities;

import java.util.List;

public interface SmellDetector {
    List<ArchitecturalSmell> detectSmells() throws NotAllowedDetection;
}
