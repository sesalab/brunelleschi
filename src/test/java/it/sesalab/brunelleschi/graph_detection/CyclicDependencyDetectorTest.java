package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.ArchitecturalSmell;
import it.sesalab.brunelleschi.core.entities.SmellType;
import it.sesalab.brunelleschi.core.entities.detector.BaseSmellDetector;
import it.sesalab.brunelleschi.core.entities.detector.SmellDetector;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CyclicDependencyDetectorTest {

    SmellDetector smellDetector;
    private FakeClassDependencyGraph fakeGraph;

    @Before
    public void setUp() throws Exception {
        smellDetector = new BaseSmellDetector();
        fakeGraph = new FakeClassDependencyGraph(false);
    }

    @Test
    public void testOneCycleDetector() {

        smellDetector = new CyclicDependencyDetector(smellDetector, fakeGraph);
        List<ArchitecturalSmell> architecturalSmells = smellDetector.detectSmells();
        assertEquals(1, architecturalSmells.size());

        ArchitecturalSmell smell = architecturalSmells.get(0);
        ArchitecturalSmell expectedSmell = new ArchitecturalSmell(SmellType.CYCLIC_DEPENDENCY);
        expectedSmell.addAffectedComponents(fakeGraph.getComponents());
        assertEquals(expectedSmell, smell);
    }
}