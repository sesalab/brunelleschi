package it.sesalab.brunelleschi.entities;

import java.util.List;

public interface SmellDetector {
    List<? extends ArchitecturalSmell> detectSmells();
}
