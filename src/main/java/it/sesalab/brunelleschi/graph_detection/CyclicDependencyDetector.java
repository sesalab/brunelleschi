package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.ArchitecturalSmell;
import it.sesalab.brunelleschi.core.entities.detector.SmellDetector;
import it.sesalab.brunelleschi.core.entities.SmellType;
import it.sesalab.brunelleschi.core.entities.Component;

import java.util.List;


public class CyclicDependencyDetector extends GraphBasedDetector {

    public CyclicDependencyDetector(SmellDetector baseDetector, DependencyGraph projectGraph) {
        super(baseDetector, projectGraph);
    }

    @Override
    public List<ArchitecturalSmell> detectSmells() {
        List<ArchitecturalSmell> result = baseDetector.detectSmells();
        for (List<Component> stronglyConnectedComponents : this.projectGraph.getElementsInStronglyConnectedComponents()){
            if(stronglyConnectedComponents.size() > 1) {
                ArchitecturalSmell smell = new ArchitecturalSmell(SmellType.CYCLIC_DEPENDENCY);
                smell.addAffectedComponents(stronglyConnectedComponents);
                result.add(smell);
            }
        }
        return result;
    }
}
