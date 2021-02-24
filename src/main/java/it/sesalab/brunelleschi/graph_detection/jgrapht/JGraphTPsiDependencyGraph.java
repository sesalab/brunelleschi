package it.sesalab.brunelleschi.graph_detection.jgrapht;

import com.intellij.psi.*;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import it.sesalab.brunelleschi.core.entities.ComponentType;
import it.sesalab.brunelleschi.core.entities.Component;
import it.sesalab.brunelleschi.graph_detection.DependencyDescriptor;
import it.sesalab.brunelleschi.graph_detection.DependencyGraph;
import lombok.Getter;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.GabowStrongConnectivityInspector;
import org.jgrapht.alg.interfaces.StrongConnectivityAlgorithm;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.nio.Attribute;
import org.jgrapht.nio.DefaultAttribute;
import org.jgrapht.nio.dot.DOTExporter;
import org.jgrapht.nio.graphml.GraphMLExporter;
import org.jgrapht.nio.json.JSONExporter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class JGraphTPsiDependencyGraph<T extends PsiQualifiedNamedElement & PsiModifierListOwner> extends DependencyGraph {

    private final Graph<T,LabeledEdge> projectGraph;
    private final String projectName;

    public JGraphTPsiDependencyGraph(boolean isPackageGraph, Graph<T, LabeledEdge> projectGraph, String projectName) {
        super(isPackageGraph);
        this.projectGraph = projectGraph;
        this.projectName = projectName;
    }

    @Override
    public List<DependencyGraph> getStronglyConnectedComponents() {
        return getStronglyConnectedSubGraphs().stream().map(graph -> new JGraphTPsiDependencyGraph<>(isPackageGraph, graph, projectName)).collect(Collectors.toList());
    }

    @Override
    public List<List<Component>> getElementsInStronglyConnectedComponents() {
        List<Graph<T, LabeledEdge>> stronglyConnectedComponents = getStronglyConnectedSubGraphs();
        List<Set<T>> stronglyConnectedSets = stronglyConnectedComponents.stream().map(Graph::vertexSet).collect(Collectors.toList());
        return stronglyConnectedSets.stream().map(ts ->
                ts.stream().map(t ->
                        new Component(t.getQualifiedName(),componentType(),isAbstraction(t)))
                        .collect(Collectors.toList()))
                .collect(Collectors.toList());
    }

    private List<Graph<T, LabeledEdge>> getStronglyConnectedSubGraphs() {
        StrongConnectivityAlgorithm<T,LabeledEdge> connectivityAlgorithm = new GabowStrongConnectivityInspector<>(this.projectGraph);
        List<Graph<T, LabeledEdge>> stronglyConnectedComponents = connectivityAlgorithm.getStronglyConnectedComponents();
        //BAD THINGS
//        for (int i = 0; i < stronglyConnectedComponents.size(); i++) {
//
//            Graph<T, LabeledEdge> graphToExport = stronglyConnectedComponents.get(i);
//            if(graphToExport.vertexSet().size() > 1) {
//                exportGraph(graphToExport, i);
//            }
//        }
        return stronglyConnectedComponents;
    }

    //TODO: Remove ASAP and figure out a decent way to export graphs
    private void exportGraph(Graph<T, LabeledEdge> graphToExport, int index){

        String folderName = System.getProperty("user.home") + File.separator + ".brunelleschi" + File.separator + projectName;
        String imgName = folderName + File.separator + "cycle" + index + ".png";
        JGraphXAdapter<T,LabeledEdge> graphAdapter = new JGraphXAdapter<>(graphToExport);
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        try {
            BufferedImage image =
                    mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
            File folder = new File(folderName);
            if(!folder.exists()){
                folder.mkdirs();
            }
            File imgFile = new File(imgName);
            if(!imgFile.exists()){
                imgFile.createNewFile();
            }
            ImageIO.write(image, "PNG", imgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int nOfVertices() {
        return this.projectGraph.vertexSet().size();
    }

    @Override
    public int nOfEdges() {
        return this.projectGraph.edgeSet().size();
    }

    @Override
    public Collection<DependencyDescriptor> evaluateDependencies() {
        Set<DependencyDescriptor> result = new HashSet<>();
        for(T vertex: projectGraph.vertexSet()){
            DependencyDescriptor descriptor = composeDependencyDescriptor(vertex);
            result.add(descriptor);
        }
        return result;
    }

    @Override
    public Collection<Component> getUnstableComponents() {
        if(!isPackageGraph){
            throw new IllegalStateException("Method must be called on package graph");
        }
        List<Component> result = new ArrayList<>();
        for(T currentVertex: projectGraph.vertexSet()) {
            List<T> successors = Graphs.successorListOf(projectGraph, currentVertex);
            DependencyDescriptor currentVertexDescriptor = composeDependencyDescriptor(currentVertex);
            for(T successor : successors){
                DependencyDescriptor successorDescriptor = composeDependencyDescriptor(successor);
                if(currentVertexDescriptor.getInstability() <= successorDescriptor.getInstability()){
                    result.add(new Component(currentVertex.getQualifiedName(),componentType(), isAbstraction(currentVertex)));
                }
            }
        }
        return result;
    }

    private boolean isAbstraction(T vertex) {
        return vertex.hasModifierProperty(PsiModifier.ABSTRACT);
    }

    @NotNull
    private DependencyDescriptor composeDependencyDescriptor(T vertex) {
        Component abstraction = new Component(vertex.getQualifiedName(),componentType(), isAbstraction(vertex));
        int fanIn = projectGraph.inDegreeOf(vertex);
        int fanOut = projectGraph.outDegreeOf(vertex);
        return new DependencyDescriptor(abstraction, fanIn, fanOut);
    }

    @NotNull
    private ComponentType componentType() {
        return isPackageGraph? ComponentType.PACKAGE :ComponentType.CLASS;
    }
}
