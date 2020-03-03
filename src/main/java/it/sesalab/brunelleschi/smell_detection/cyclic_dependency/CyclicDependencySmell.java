package it.sesalab.brunelleschi.smell_detection.cyclic_dependency;

import com.intellij.psi.PsiClass;
import it.sesalab.brunelleschi.smell_detection.ArchitecturalSmell;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class CyclicDependencySmell extends ArchitecturalSmell {

    Set<PsiClass> cycle;

    public CyclicDependencySmell(Set<PsiClass> cycle) {
        super("cyclic dependency");
        this.cycle = cycle;
    }

    public CyclicDependencySmell() {
        this(new HashSet<>());
    }

    @Override
    public Collection<PsiClass> getSmellyComponents() {
        return cycle;
    }
}
