package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.ArchitecturalSmell;
import it.sesalab.brunelleschi.core.entities.Component;
import it.sesalab.brunelleschi.core.entities.SmellType;
import it.sesalab.brunelleschi.core.entities.detector.BaseSmellDetector;
import it.sesalab.brunelleschi.core.entities.detector.SmellDetector;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class HubLikeDependencyDetectorTest {

    SmellDetector smellDetector;
    private FakeClassGraph fakeGraph;

    @Before
    public void setUp() throws Exception {
        smellDetector = new BaseSmellDetector();
        fakeGraph = new FakeClassGraph();
    }

    @Test
    public void detectSmells() {
        smellDetector = new HubLikeDependencyDetector(smellDetector,fakeGraph,0);

        List<ArchitecturalSmell> actualSmells = smellDetector.detectSmells();
        assertEquals(2, actualSmells.size());

        List<ArchitecturalSmell> expectedSmells = fakeGraph.getComponents()
                                                .stream()
                                                .map((this::makeHubLikeFrom))
                                                .collect(Collectors.toList());

        assertEquals(expectedSmells, actualSmells);
    }

    @NotNull
    private ArchitecturalSmell makeHubLikeFrom(Component component) {
        ArchitecturalSmell smell = new ArchitecturalSmell(SmellType.HUB_LIKE_DEPENDENCY);
        smell.addAffectedComponent(component);
        return smell;
    }
}