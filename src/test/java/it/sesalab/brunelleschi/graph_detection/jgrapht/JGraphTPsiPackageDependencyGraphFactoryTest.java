package it.sesalab.brunelleschi.graph_detection.jgrapht;

import com.intellij.psi.PsiPackage;
import com.intellij.testFramework.fixtures.LightJavaCodeInsightFixtureTestCase;
import it.sesalab.brunelleschi.graph_detection.DependencyGraph;
import it.sesalab.brunelleschi.graph_detection.DependencyGraphFactory;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.io.File;

public class JGraphTPsiPackageDependencyGraphFactoryTest extends LightJavaCodeInsightFixtureTestCase {

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
        myFixture.configureByFiles( "packageGraph/a/A.java","packageGraph/b/B.java");
    }

    public void testBuildGraph() {
        PsiPackage aPackage = myFixture.findPackage("packageGraph.a");
        PsiPackage bPackage = myFixture.findPackage("packageGraph.b");

        Graph<PsiPackage, LabeledEdge> oracleGraph = new DefaultDirectedGraph<>(LabeledEdge.class);
        oracleGraph.addVertex(aPackage);
        oracleGraph.addVertex(bPackage);
        oracleGraph.addEdge(aPackage,bPackage, new LabeledEdge("dependency"));

        DependencyGraphFactory factory = new JGraphTPsiPackageDependencyGraphFactory(myFixture.getProject());
        DependencyGraph dependencyGraph = factory.makeDependencyGraph();

        assertEquals(oracleGraph, ((JGraphTPsiDependencyGraph) dependencyGraph).getProjectGraph());
    }
}