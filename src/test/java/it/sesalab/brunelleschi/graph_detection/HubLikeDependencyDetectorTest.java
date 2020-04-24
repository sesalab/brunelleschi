package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.detector.BaseSmellDetector;
import it.sesalab.brunelleschi.core.entities.detector.SmellDetector;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HubLikeDependencyDetectorTest {

    SmellDetector smellDetector;
    private FakeClassDependencyGraph fakeGraph;

    @Before
    public void setUp() throws Exception {
        smellDetector = new BaseSmellDetector();
        fakeGraph = new FakeClassDependencyGraph(false);
    }

    @Test
    public void detectSmells() {
        smellDetector = new HubLikeDependencyDetector(smellDetector,fakeGraph,0);
    }
}