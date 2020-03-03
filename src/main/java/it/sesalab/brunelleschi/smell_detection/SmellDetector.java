package it.sesalab.brunelleschi.smell_detection;

import com.intellij.psi.PsiElement;

import java.util.Collection;

public abstract class SmellDetector{

    public abstract Collection<? extends ArchitecturalSmell> getSmells();
}
