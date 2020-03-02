package it.sesalab.brunelleschi.graph;

import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Query;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultDirectedGraph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Value
@EqualsAndHashCode(callSuper = true)
public class JGraphTDependencyGraph extends DependencyGraph {


    private final Graph<PsiClass,LabeledEdge> projectGraph;

    public JGraphTDependencyGraph(Project currentProject) {
        super(currentProject);
        this.projectGraph = new DefaultDirectedGraph<>(LabeledEdge.class);
        buildClassDependencyGraph();
    }

    @Override
    protected void buildClassDependencyGraph(){
        for (PsiPackage aPackage : allProjectJavaPackages()) {
            for (PsiClass currentClass : aPackage.getClasses()) {
                projectGraph.addVertex(currentClass);

                addDependenciesOf(currentClass);
            }
        }
    }

    private void addDependenciesOf(PsiClass currentClass) {
        Query<PsiReference> search = ReferencesSearch.search(currentClass);
        for (PsiReference ref : search.findAll()) {
            PsiClass dependentClass = PsiTreeUtil.getParentOfType(ref.getElement(), PsiClass.class);
            projectGraph.addVertex(dependentClass);
            log.debug("Found dependent class "+ dependentClass.getQualifiedName()+" of "+currentClass.getQualifiedName() );

            LabeledEdge edge;
            if(dependentClass.getSuperClass() != null && dependentClass.getSuperClass().equals(currentClass)){
                edge = new LabeledEdge(EXTENDS_LABEL);
            } else {
                edge = new LabeledEdge(DEPENDENCY_LABEL);
            }
            projectGraph.addEdge(dependentClass, currentClass , edge);

        }
    }

    @Override
    public Map<PsiClass, List<PsiClass>> adjacencyList() {
        Map<PsiClass, List<PsiClass>> adjacencyList = new HashMap<>();
        for(PsiClass node: projectGraph.vertexSet()){
            adjacencyList.put(node, Graphs.neighborListOf(projectGraph,node));
        }
        return adjacencyList;
    }
}
