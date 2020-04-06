package it.sesalab.brunelleschi.entities;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
public class ArchitecturalSmell {
    protected final SmellType smellType;
    protected final List<SwComponent> affectedComponents;

    public ArchitecturalSmell(SmellType smellType) {
        this.smellType = smellType;
        this.affectedComponents = new ArrayList<>();
    }

    public void addAffectedComponent(SwComponent component){
        this.affectedComponents.add(component);
    }

    public void addAffectedComponents(Collection<? extends SwComponent> components){
        this.affectedComponents.addAll(components);
    }
}
