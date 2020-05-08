package it.sesalab.brunelleschi.core.usecases.findsmell;

import it.sesalab.brunelleschi.core.entities.ArchitecturalSmell;
import it.sesalab.brunelleschi.core.entities.detector.SmellDetector;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor
public class FindSmellInteractor {

    private final SmellDetectorBuildingCommand buildingCommand;

    public Collection<ArchitecturalSmell> execute(){

        SmellDetector smellDetector = buildingCommand.buildSmellDetector();
        return smellDetector.detectSmells();
    }

}
