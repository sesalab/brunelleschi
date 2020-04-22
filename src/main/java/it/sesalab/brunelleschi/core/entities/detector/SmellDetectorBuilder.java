package it.sesalab.brunelleschi.core.entities.detector;

public interface SmellDetectorBuilder {

    SmellDetectorBuilder enableCyclicDependencyDetection();

    SmellDetectorBuilder enableHubLikeDependencyDetection(int threshold);

    // TODO: SmellDetectorBuilder enableUnstableDependencyDetection();

    SmellDetector build();

}
