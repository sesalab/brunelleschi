package it.sesalab.brunelleschi.graph_detection.cyclic_dependency;

import it.sesalab.brunelleschi.core.entities.ArchitecturalSmell;
import it.sesalab.brunelleschi.core.entities.SmellType;
import it.sesalab.brunelleschi.graph_detection.CyclicDependencyDetector;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CyclicDependencyDetectorTest {

    @Test
    public void testOneCycleDetector() {
        FakeClassDependencyGraph fakeGraph = new FakeClassDependencyGraph(false);
        CyclicDependencyDetector detector = new CyclicDependencyDetector(fakeGraph);
        List<? extends ArchitecturalSmell> architecturalSmells = detector.detectSmells();
        assertEquals(1, architecturalSmells.size());

        ArchitecturalSmell smell = architecturalSmells.get(0);
        ArchitecturalSmell expectedSmell = new ArchitecturalSmell(SmellType.CYCLIC_DEPENDENCY);
        expectedSmell.addAffectedComponents(fakeGraph.getComponents());
        assertEquals(expectedSmell, smell);
    }
}