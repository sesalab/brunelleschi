package it.sesalab.brunelleschi.graph_detection.jgrapht;

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import it.sesalab.brunelleschi.core.entities.ComponentType;
import it.sesalab.brunelleschi.core.entities.Component;
import it.sesalab.brunelleschi.graph_detection.DependencyDescriptor;
import it.sesalab.brunelleschi.graph_detection.DependencyGraph;
import it.sesalab.brunelleschi.graph_detection.DependencyGraphFactory;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class JGraphTPsiDependencyGraphTest extends LightJavaCodeInsightFixtureTestCase {

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

        assertEquals(oracle, tinyCycleGraph.getCycles());
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

        assertEquals(oracle, tinyCycleGraph.getCycles());
    }

    public void testGraphWithTwoCycles() {
        myFixture.configureByFiles(
                "cycles/simple/A.java",
                "cycles/simple/B.java",
                "cycles/simple/C.java",
                "cycles/simple/Glue.java",
                "cycles/tiny/A.java",
                "cycles/tiny/B.java");

        Set<Set<Component>> oracle = new HashSet<>();

        Set<Component> simpleCycle = new HashSet<>();
        simpleCycle.add(new Component("cycles.simple.A", ComponentType.CLASS));
        simpleCycle.add(new Component("cycles.simple.B", ComponentType.CLASS));
        simpleCycle.add(new Component("cycles.simple.C", ComponentType.CLASS));
        oracle.add(simpleCycle);

        Set<Component> tinyCycle = new HashSet<>();
        tinyCycle.add(new Component("cycles.tiny.A", ComponentType.CLASS));
        tinyCycle.add(new Component("cycles.tiny.B", ComponentType.CLASS));
        oracle.add(tinyCycle);

        DependencyGraphFactory factory = new JGraphTPsiClassDependencyGraphFactory(myFixture.getProject());
        DependencyGraph tinyCycleGraph = factory.makeDependencyGraph();

        Set<Set<Component>> detectedCycles = tinyCycleGraph.getCycles();
        assertEquals(oracle, detectedCycles);

    }

    public void testClique() {
        myFixture.configureByFiles( "cycles/clique/A.java","cycles/clique/B.java","cycles/clique/C.java");

        DependencyGraphFactory factory = new JGraphTPsiClassDependencyGraphFactory(myFixture.getProject());
        DependencyGraph dependencyGraph = factory.makeDependencyGraph();

        Set<Set<Component>> detectedCycles = dependencyGraph.getCycles();

        int numberOfVertices = dependencyGraph.nOfVertices();
        int expectedNofCycles = (int) Math.pow(2,numberOfVertices) - (1 + numberOfVertices);

        assertEquals(expectedNofCycles, detectedCycles.size());

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