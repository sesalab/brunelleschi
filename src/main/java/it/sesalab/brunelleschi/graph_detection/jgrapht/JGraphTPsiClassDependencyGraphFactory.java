package it.sesalab.brunelleschi.graph_detection.jgrapht;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiPackage;
import it.sesalab.brunelleschi.graph_detection.DependencyGraph;
import org.jetbrains.annotations.NotNull;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.builder.GraphBuilder;


public class JGraphTPsiClassDependencyGraphFactory extends JGraphTPsiDependencyGraphFactory {

    private GraphBuilder<PsiClass, LabeledEdge, ? extends DefaultDirectedGraph<PsiClass, LabeledEdge>> graphBuilder;

    public JGraphTPsiClassDependencyGraphFactory(Project currentProject) {
        super(currentProject);
    }

    @Override
    public DependencyGraph makeDependencyGraph() {
        //TODO figure out how to reuse more of the code below
        graphBuilder = DefaultDirectedGraph.createBuilder(LabeledEdge.class);
            for (PsiClass currentClass : projectClasses) {
                if(classIsLegit(currentClass)) {
                    graphBuilder.addVertex(currentClass);
                    for (PsiClass dependentClass : findDependentClasses(currentClass)) {
                        graphBuilder.addVertex(dependentClass);
                        makeEdgeBetween(currentClass, dependentClass);
                    }
                }
        }
        return new JGraphTPsiDependencyGraph<>(false, graphBuilder.build(), currentProject.getName());
    }

    private void makeEdgeBetween(PsiClass currentClass, PsiClass dependentClass) {
        LabeledEdge edge = makeEdge(currentClass, dependentClass);
        graphBuilder.addEdge(currentClass, dependentClass, edge);
    }

    @NotNull
    protected LabeledEdge makeEdge(PsiClass currentClass, PsiClass dependentClass) {
        LabeledEdge edge;
        if(currentClass.getSuperClass() != null && currentClass.getSuperClass().equals(dependentClass)){
            edge = new LabeledEdge(EXTENDS_LABEL);
        } else {
            edge = new LabeledEdge(DEPENDENCY_LABEL);
        }
        return edge;
    }

}
