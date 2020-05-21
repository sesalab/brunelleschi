package it.sesalab.brunelleschi.core.usecases.findsmell;

import it.sesalab.brunelleschi.core.entities.detector.SmellDetector;
import it.sesalab.brunelleschi.core.entities.detector.SmellDetectorBuilder;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FindAllSmellBuildingCommand implements SmellDetectorBuildingCommand {

    private final SmellDetectorBuilder smellDetectorBuilder;
    private final int hubLikeThreshold;

    @Override
    public SmellDetector buildSmellDetector() {
        return smellDetectorBuilder
                .enableUnstableDependencyDetection()
                .enableHubLikeDependencyDetection(hubLikeThreshold)
                .enableCyclicDependencyDetection()
                .build();
    }
}
