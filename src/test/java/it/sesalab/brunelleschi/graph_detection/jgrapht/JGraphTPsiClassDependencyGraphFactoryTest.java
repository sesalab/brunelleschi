package it.sesalab.brunelleschi.graph_detection.jgrapht;


import com.intellij.psi.PsiClass;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import it.sesalab.brunelleschi.graph_detection.DependencyGraph;
import it.sesalab.brunelleschi.graph_detection.DependencyGraphFactory;
import org.apache.log4j.Logger;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.SimpleDirectedGraph;

import java.io.File;

public class JGraphTPsiClassDependencyGraphFactoryTest extends LightJavaCodeInsightFixtureTestCase {

    Logger log = Logger.getLogger(this.getClass());

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

    @Override
    public void setUp() throws Exception {
        super.setUp();
        myFixture.configureByFiles( "graph/A.java","graph/B.java");
    }

    public void testClassAIsPresent() {
        PsiClass aClass = myFixture.findClass("graph.A");
        PsiClass bClass = myFixture.findClass("graph.A");
        log.debug(aClass.toString());
        assertEquals(aClass,bClass);
        assertEquals(aClass.getQualifiedName(),"graph.A");
    }

    public void testGraphAreEqual() {
        PsiClass classA = myFixture.findClass("graph.A");
        PsiClass classB = myFixture.findClass("graph.B");

        Graph<PsiClass, LabeledEdge> oracleGraph = new DefaultDirectedGraph<>(LabeledEdge.class);
        oracleGraph.addVertex(classA);
        oracleGraph.addVertex(classB);
        oracleGraph.addEdge(classB,classA,new LabeledEdge("dependency"));

        DependencyGraphFactory factory = new JGraphTPsiClassDependencyGraphFactory(myFixture.getProject());
        DependencyGraph dependencyGraph = factory.makeDependencyGraph();

        assertEquals(oracleGraph, ((JGraphTPsiDependencyGraph) dependencyGraph).getProjectGraph());
    }


    public void testGraphAreDifferentWithDifferentNumberOfVertex() {
        PsiClass classA = myFixture.findClass("graph.A");

        Graph<PsiClass,LabeledEdge> oracleGraph = new SimpleDirectedGraph<>(LabeledEdge.class);
        oracleGraph.addVertex(classA);

        DependencyGraphFactory factory = new JGraphTPsiClassDependencyGraphFactory(myFixture.getProject());
        DependencyGraph dependencyGraph = factory.makeDependencyGraph();

        assertFalse(oracleGraph.equals(((JGraphTPsiDependencyGraph) dependencyGraph).getProjectGraph()));
    }

    public void testGraphAreDifferentWithInvertedEdges() {
        PsiClass classA = myFixture.findClass("graph.A");
        PsiClass classB = myFixture.findClass("graph.B");

        Graph<PsiClass,LabeledEdge> oracleGraph = new SimpleDirectedGraph<>(LabeledEdge.class);
        oracleGraph.addVertex(classA);
        oracleGraph.addVertex(classB);
        oracleGraph.addEdge(classA,classB,new LabeledEdge("dependency"));

        DependencyGraphFactory factory = new JGraphTPsiClassDependencyGraphFactory(myFixture.getProject());
        DependencyGraph dependencyGraph = factory.makeDependencyGraph();

        assertFalse(oracleGraph.equals(((JGraphTPsiDependencyGraph) dependencyGraph).getProjectGraph()));
    }


    public void testThreeItemsGraph() {
        myFixture.copyFileToProject("graph/C.java");

        PsiClass classA = myFixture.findClass("graph.A");
        PsiClass classB = myFixture.findClass("graph.B");
        PsiClass classC = myFixture.findClass("graph.C");

        Graph<PsiClass,LabeledEdge> oracleGraph = new DefaultDirectedGraph<>(LabeledEdge.class);
        oracleGraph.addVertex(classA);
        oracleGraph.addVertex(classB);
        oracleGraph.addVertex(classC);

        oracleGraph.addEdge(classB,classA,new LabeledEdge("dependency"));
        oracleGraph.addEdge(classC,classA,new LabeledEdge("dependency"));
        oracleGraph.addEdge(classC,classB,new LabeledEdge("dependency"));

        DependencyGraphFactory factory = new JGraphTPsiClassDependencyGraphFactory(myFixture.getProject());
        DependencyGraph dependencyGraph = factory.makeDependencyGraph();

        assertEquals(oracleGraph, ((JGraphTPsiDependencyGraph) dependencyGraph).getProjectGraph());
    }

    public void testExtendsRelation() {
        myFixture.copyFileToProject("graph/BChild.java");

        PsiClass classA = myFixture.findClass("graph.A");
        PsiClass classB = myFixture.findClass("graph.B");
        PsiClass classBChild = myFixture.findClass("graph.BChild");

        Graph<PsiClass,LabeledEdge> oracleGraph = new DefaultDirectedGraph<>(LabeledEdge.class);
        oracleGraph.addVertex(classA);
        oracleGraph.addVertex(classB);
        oracleGraph.addVertex(classBChild);

        oracleGraph.addEdge(classB,classA,new LabeledEdge("dependency"));
        oracleGraph.addEdge(classBChild,classB,new LabeledEdge("extends"));

        DependencyGraphFactory factory = new JGraphTPsiClassDependencyGraphFactory(myFixture.getProject());
        DependencyGraph dependencyGraph = factory.makeDependencyGraph();

        assertEquals(oracleGraph, ((JGraphTPsiDependencyGraph) dependencyGraph).getProjectGraph());
    }
}