package it.sesalab.brunelleschi.core.entities.detector;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractSmellDetectorDecorator implements SmellDetector {
    protected final SmellDetector baseDetector;
}
