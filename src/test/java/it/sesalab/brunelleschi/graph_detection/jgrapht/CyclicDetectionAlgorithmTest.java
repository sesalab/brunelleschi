package it.sesalab.brunelleschi.graph_detection.jgrapht;

import org.jgrapht.Graph;
import org.jgrapht.alg.connectivity.GabowStrongConnectivityInspector;
import org.jgrapht.alg.connectivity.KosarajuStrongConnectivityInspector;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.builder.GraphBuilder;
import org.junit.Before;
import org.junit.Test;


import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CyclicDetectionAlgorithmTest {

    private Graph<Integer, DefaultEdge> testGraph;

    @Before
    public void setup(){
       GraphBuilder<Integer, DefaultEdge, ? extends DefaultDirectedGraph<Integer, DefaultEdge>> builder = DefaultDirectedGraph.createBuilder(DefaultEdge.class);
       builder.addVertex(0);
       builder.addVertex(1);
       builder.addVertex(2);

       builder.addEdge(0,1);
       builder.addEdge(0,2);
       builder.addEdge(1,2);

       testGraph = builder.build();
    }

    @Test
    public void shouldNotReturnAStronglyConnectedComponent() {
        System.out.println(testGraph);
        StrongConnectivityAlgorithm<Integer,DefaultEdge> algorithm = new KosarajuStrongConnectivityInspector<>(testGraph);
        List<Graph<Integer, DefaultEdge>> stronglyConnectedComponents = algorithm.getStronglyConnectedComponents();
        assertThat(stronglyConnectedComponents).isNotEmpty();
    }
}
