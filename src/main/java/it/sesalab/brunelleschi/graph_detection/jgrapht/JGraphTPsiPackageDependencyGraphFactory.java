package it.sesalab.brunelleschi.graph_detection.jgrapht;

import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiPackage;
import it.sesalab.brunelleschi.graph_detection.DependencyGraph;
import it.sesalab.brunelleschi.graph_detection.DependencyGraphFactory;
import lombok.RequiredArgsConstructor;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.builder.GraphBuilder;

@RequiredArgsConstructor
public class JGraphTPsiPackageDependencyGraphFactory extends DependencyGraphFactory {

    private final PsiPackageExtractor packageExtractor;
    private GraphBuilder<PsiPackage, LabeledEdge, ? extends DefaultDirectedGraph<PsiPackage, LabeledEdge>> graphBuilder;

    @Override
    public DependencyGraph makeDependencyGraph() {
        return null;
    }
}
