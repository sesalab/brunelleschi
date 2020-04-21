package it.sesalab.brunelleschi.graph_detection.jgrapht;

import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import it.sesalab.brunelleschi.core.entities.ComponentType;
import it.sesalab.brunelleschi.core.entities.SwComponent;
import it.sesalab.brunelleschi.graph_detection.DependencyGraph;
import it.sesalab.brunelleschi.graph_detection.DependencyGraphFactory;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
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

        Set<SwComponent> tinyCycle = new HashSet<>();
        Set<Set<SwComponent>> oracle = new HashSet<>();
        tinyCycle.add(new SwComponent("cycles.tiny.A", ComponentType.CLASS));
        tinyCycle.add(new SwComponent("cycles.tiny.B", ComponentType.CLASS));
        oracle.add(tinyCycle);

        PsiPackageExtractor psiPackageExtractor = new PsiPackageExtractor(myFixture.getProject());
        DependencyGraphFactory factory = new JGraphTPsiClassDependencyGraphFactory(psiPackageExtractor);
        DependencyGraph tinyCycleGraph = factory.makeDependencyGraph();

        assertEquals(oracle, tinyCycleGraph.getCycles());
    }

    public void testSimpleCycleDetection() {
        myFixture.configureByFiles( "cycles/simple/A.java","cycles/simple/B.java","cycles/simple/C.java");

        Set<SwComponent> cycle = new HashSet<>();
        Set<Set<SwComponent>> oracle = new HashSet<>();
        cycle.add(new SwComponent("cycles.simple.A", ComponentType.CLASS));
        cycle.add(new SwComponent("cycles.simple.B", ComponentType.CLASS));
        cycle.add(new SwComponent("cycles.simple.C", ComponentType.CLASS));
        oracle.add(cycle);

        PsiPackageExtractor psiPackageExtractor = new PsiPackageExtractor(myFixture.getProject());
        DependencyGraphFactory factory = new JGraphTPsiClassDependencyGraphFactory(psiPackageExtractor);
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

        Set<Set<SwComponent>> oracle = new HashSet<>();

        Set<SwComponent> simpleCycle = new HashSet<>();
        simpleCycle.add(new SwComponent("cycles.simple.A", ComponentType.CLASS));
        simpleCycle.add(new SwComponent("cycles.simple.B", ComponentType.CLASS));
        simpleCycle.add(new SwComponent("cycles.simple.C", ComponentType.CLASS));
        oracle.add(simpleCycle);

        Set<SwComponent> tinyCycle = new HashSet<>();
        tinyCycle.add(new SwComponent("cycles.tiny.A", ComponentType.CLASS));
        tinyCycle.add(new SwComponent("cycles.tiny.B", ComponentType.CLASS));
        oracle.add(tinyCycle);

        PsiPackageExtractor psiPackageExtractor = new PsiPackageExtractor(myFixture.getProject());
        DependencyGraphFactory factory = new JGraphTPsiClassDependencyGraphFactory(psiPackageExtractor);
        DependencyGraph tinyCycleGraph = factory.makeDependencyGraph();

        Set<Set<SwComponent>> detectedCycles = tinyCycleGraph.getCycles();
        assertEquals(oracle, detectedCycles);

    }

    public void testClique() {
        myFixture.configureByFiles( "cycles/clique/A.java","cycles/clique/B.java","cycles/clique/C.java");

        PsiPackageExtractor psiPackageExtractor = new PsiPackageExtractor(myFixture.getProject());
        DependencyGraphFactory factory = new JGraphTPsiClassDependencyGraphFactory(psiPackageExtractor);
        DependencyGraph dependencyGraph = factory.makeDependencyGraph();

        Set<Set<SwComponent>> detectedCycles = dependencyGraph.getCycles();

        int numberOfVertices = dependencyGraph.nOfVertices();
        int expectedNofCycles = (int) Math.pow(2,numberOfVertices) - (1 + numberOfVertices);

        assertEquals(expectedNofCycles, detectedCycles.size());

    }

    public void testAbstractionsDependenciesMap() {
        myFixture.configureByFiles( "hublike/A.java","hublike/B.java","hublike/Abstract.java");

        PsiPackageExtractor psiPackageExtractor = new PsiPackageExtractor(myFixture.getProject());
        DependencyGraphFactory factory = new JGraphTPsiClassDependencyGraphFactory(psiPackageExtractor);
        DependencyGraph dependencyGraph = factory.makeDependencyGraph();

        SwComponent oracleAbstractClass = new SwComponent("hublike.Abstract", ComponentType.CLASS);
        Map<SwComponent,Integer> oracle = Map.of(oracleAbstractClass,2);


        assertEquals(oracle, dependencyGraph.abstractionsDependenciesMap());
    }
}