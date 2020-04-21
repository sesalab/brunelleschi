package it.sesalab.brunelleschi.graph_detection.jgrapht;

import com.intellij.psi.*;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Query;
import it.sesalab.brunelleschi.graph_detection.DependencyGraph;
import it.sesalab.brunelleschi.graph_detection.DependencyGraphFactory;
import lombok.RequiredArgsConstructor;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.builder.GraphBuilder;

@RequiredArgsConstructor
public class JGraphTPsiClassDependencyGraphFactory  extends DependencyGraphFactory {

    private final PsiPackageExtractor psiPackageExtractor;
    private GraphBuilder<PsiClass, LabeledEdge, ? extends DefaultDirectedGraph<PsiClass, LabeledEdge>> graphBuilder;

    @Override
    public DependencyGraph makeDependencyGraph() {
        graphBuilder = DefaultDirectedGraph.createBuilder(LabeledEdge.class);
        for (PsiPackage aPackage : psiPackageExtractor.allProjectJavaPackages()) {
            for (PsiClass currentClass : aPackage.getClasses()) {
                graphBuilder.addVertex(currentClass);
                addDependenciesOf(currentClass);
            }
        }
        return new JGraphTPsiDependencyGraph<>(false, graphBuilder.build());
    }

    protected void addDependenciesOf(PsiClass currentClass) {
        Query<PsiReference> search = ReferencesSearch.search(currentClass);
        for (PsiReference ref : search.findAll()) {
            PsiClass dependentClass = PsiTreeUtil.getParentOfType(ref.getElement(), PsiClass.class);
            graphBuilder.addVertex(dependentClass);

            LabeledEdge edge;
            if(dependentClass.getSuperClass() != null && dependentClass.getSuperClass().equals(currentClass)){
                edge = new LabeledEdge(EXTENDS_LABEL);
            } else {
                edge = new LabeledEdge(DEPENDENCY_LABEL);
            }
            graphBuilder.addEdge(dependentClass, currentClass, edge);

        }
    }

}
