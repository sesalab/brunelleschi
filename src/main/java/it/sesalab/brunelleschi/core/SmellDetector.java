package it.sesalab.brunelleschi.core;

import java.util.List;

public interface SmellDetector {
    List<? extends ArchitecturalSmell> detectSmells() throws NotAllowedDetection;
}
