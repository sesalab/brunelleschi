package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.ArchitecturalSmell;
import it.sesalab.brunelleschi.core.entities.Component;
import it.sesalab.brunelleschi.core.entities.ComponentType;
import it.sesalab.brunelleschi.core.entities.SmellType;
import it.sesalab.brunelleschi.core.entities.detector.BaseSmellDetector;
import it.sesalab.brunelleschi.core.entities.detector.SmellDetector;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UnstableDependencyDetectorTest {

    @Test
    public void detectSmells() {

        FakePackageGraph graph = new FakePackageGraph();
        BaseSmellDetector baseSmellDetector = new BaseSmellDetector();
        SmellDetector unstableDependencyDetector = new UnstableDependencyDetector(baseSmellDetector,graph,0.3);

        Component smellyComponent = new Component("unstableDependency", ComponentType.PACKAGE);
        ArchitecturalSmell oracle = new ArchitecturalSmell(SmellType.UNSTABLE_DEPENDENCY);
        oracle.addAffectedComponent(smellyComponent);

        List<ArchitecturalSmell> detectedSmells = unstableDependencyDetector.detectSmells();

        assertEquals(1,detectedSmells.size());
        assertEquals(oracle,detectedSmells.get(0));

    }
}