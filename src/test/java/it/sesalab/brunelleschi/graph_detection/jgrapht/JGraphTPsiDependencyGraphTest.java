package it.sesalab.brunelleschi.graph_detection.jgrapht;

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import it.sesalab.brunelleschi.core.entities.ComponentType;
import it.sesalab.brunelleschi.core.entities.Component;
import it.sesalab.brunelleschi.graph_detection.DependencyDescriptor;
import it.sesalab.brunelleschi.graph_detection.DependencyGraph;
import it.sesalab.brunelleschi.graph_detection.DependencyGraphFactory;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class JGraphTPsiDependencyGraphTest extends LightJavaCodeInsightFixtureTestCase {
//TODO: Rename Cycles with strongly connected components
    @Override
    protected String getTestDataPath() {
        StringBuilder path = new StringBuilder();
        path
                .append(".")
                .append(File.separator)
                .append("src")
                .append(File.separator)
                .append("test")
                .append(File.separator)
                .append("testData");
        return path.toString();
    }

    public void testTinyCycleDetection() {
        myFixture.configureByFiles( "cycles/tiny/A.java","cycles/tiny/B.java");

        Set<Component> tinyCycle = new HashSet<>();
        Set<Set<Component>> oracle = new HashSet<>();
        tinyCycle.add(new Component("cycles.tiny.A", ComponentType.CLASS));
        tinyCycle.add(new Component("cycles.tiny.B", ComponentType.CLASS));
        oracle.add(tinyCycle);

        DependencyGraphFactory factory = new JGraphTPsiClassDependencyGraphFactory(myFixture.getProject());
        DependencyGraph tinyCycleGraph = factory.makeDependencyGraph();

        List<List<Component>> cycles = tinyCycleGraph.getStronglyConnectedComponents();
        assertEquals(oracle, convertCycleListToSet(cycles));
    }

    public void testSimpleCycleDetection() {
        myFixture.configureByFiles( "cycles/simple/A.java","cycles/simple/B.java","cycles/simple/C.java");

        Set<Component> cycle = new HashSet<>();
        Set<Set<Component>> oracle = new HashSet<>();
        cycle.add(new Component("cycles.simple.A", ComponentType.CLASS));
        cycle.add(new Component("cycles.simple.B", ComponentType.CLASS));
        cycle.add(new Component("cycles.simple.C", ComponentType.CLASS));
        oracle.add(cycle);

        DependencyGraphFactory factory = new JGraphTPsiClassDependencyGraphFactory(myFixture.getProject());
        DependencyGraph tinyCycleGraph = factory.makeDependencyGraph();

        List<List<Component>> cycles = tinyCycleGraph.getStronglyConnectedComponents();
        Set<Set<Component>> actual = convertCycleListToSet(cycles);
        assertEquals(oracle,actual);
    }

    @NotNull
    private Set<Set<Component>> convertCycleListToSet(List<List<Component>> cycles) {
        return cycles.stream().map(Set::copyOf).collect(Collectors.toSet());
    }

    public void testGraphWithTwoCycles() {
        myFixture.configureByFiles(
                "cycles/simple/A.java",
                "cycles/simple/B.java",
                "cycles/simple/C.java",
                "cycles/simple/Glue.java",
                "cycles/tiny/A.java",
                "cycles/tiny/B.java");

        List<List<Component>> oracle = new ArrayList<>();

        List<Component> simpleCycle = new ArrayList<>();
        simpleCycle.add(new Component("cycles.simple.A", ComponentType.CLASS));
        simpleCycle.add(new Component("cycles.simple.B", ComponentType.CLASS));
        simpleCycle.add(new Component("cycles.simple.C", ComponentType.CLASS));
        oracle.add(simpleCycle);

        List<Component> tinyCycle = new ArrayList<>();
        tinyCycle.add(new Component("cycles.tiny.A", ComponentType.CLASS));
        tinyCycle.add(new Component("cycles.tiny.B", ComponentType.CLASS));
        oracle.add(tinyCycle);

        List<Component> single = new ArrayList<>();
        single.add(new Component("cycles.simple.Glue", ComponentType.CLASS));
        oracle.add(single);

        DependencyGraphFactory factory = new JGraphTPsiClassDependencyGraphFactory(myFixture.getProject());
        DependencyGraph tinyCycleGraph = factory.makeDependencyGraph();

        List<List<Component>> detectedCycles = tinyCycleGraph.getStronglyConnectedComponents();
        assertEquals(convertCycleListToSet(oracle), convertCycleListToSet(detectedCycles));

    }

    public void testClique() {
        myFixture.configureByFiles( "cycles/clique/A.java","cycles/clique/B.java","cycles/clique/C.java");

        DependencyGraphFactory factory = new JGraphTPsiClassDependencyGraphFactory(myFixture.getProject());
        DependencyGraph dependencyGraph = factory.makeDependencyGraph();

        List<List<Component>> detectedCycles = dependencyGraph.getStronglyConnectedComponents();

        int numberOfVertices = dependencyGraph.nOfVertices();
        int nOfVerticesInClique = detectedCycles.get(0).size();
        assertEquals(1,detectedCycles.size());
        assertEquals( numberOfVertices,nOfVerticesInClique);

    }

    public void testDependencyDescriptorGeneration() {
        myFixture.configureByFiles( "hublike/A.java","hublike/B.java","hublike/Abstract.java");

        DependencyGraphFactory factory = new JGraphTPsiClassDependencyGraphFactory(myFixture.getProject());
        DependencyGraph dependencyGraph = factory.makeDependencyGraph();

        Component oracleAbstractClass = new Component("hublike.Abstract", ComponentType.CLASS, true);
        Component oracleAClass = new Component("hublike.A", ComponentType.CLASS);
        Component oracleBClass = new Component("hublike.B", ComponentType.CLASS);

        DependencyDescriptor abstractClassDescriptor = new DependencyDescriptor(oracleAbstractClass,1,1);
        DependencyDescriptor aClassDescriptor = new DependencyDescriptor(oracleAClass,1,0);
        DependencyDescriptor bClassDescriptor = new DependencyDescriptor(oracleBClass,0,1);

        Set<DependencyDescriptor> oracle = Set.of(abstractClassDescriptor,aClassDescriptor,bClassDescriptor);

        assertEquals(oracle, dependencyGraph.evaluateDependencies());
    }
}