package it.sesalab.brunelleschi.core.entities;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.*;

@Getter
@EqualsAndHashCode
public class ArchitecturalSmell {
    protected final SmellType smellType;
    protected final Set<Component> affectedComponents;

    public ArchitecturalSmell(SmellType smellType) {
        this.smellType = smellType;
        this.affectedComponents = new LinkedHashSet<>();
    }

    public void addAffectedComponent(Component component){
        this.affectedComponents.add(component);
    }

    public void addAffectedComponents(Collection<? extends Component> components){
        this.affectedComponents.addAll(components);
    }
}
