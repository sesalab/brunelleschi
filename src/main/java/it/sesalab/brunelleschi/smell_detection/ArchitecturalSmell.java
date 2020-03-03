package it.sesalab.brunelleschi.smell_detection;

import com.intellij.psi.PsiElement;
import lombok.Data;

import java.util.Collection;

@Data
public abstract class ArchitecturalSmell {

    protected final String name;

    public abstract Collection<? extends PsiElement> getSmellyComponents();

}
