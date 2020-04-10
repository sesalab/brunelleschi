package it.sesalab.brunelleschi.core;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;

@Getter
@EqualsAndHashCode
public class ArchitecturalSmell {
    protected final SmellType smellType;
    protected final Set<SwComponent> affectedComponents;

    public ArchitecturalSmell(SmellType smellType) {
        this.smellType = smellType;
        this.affectedComponents = new HashSet<>();
    }

    public void addAffectedComponent(SwComponent component){
        this.affectedComponents.add(component);
    }

    public void addAffectedComponents(Collection<? extends SwComponent> components){
        this.affectedComponents.addAll(components);
    }
}
