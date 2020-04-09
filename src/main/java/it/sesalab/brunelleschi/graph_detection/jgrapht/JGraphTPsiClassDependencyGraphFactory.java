package it.sesalab.brunelleschi.graph_detection.jgrapht;

import com.intellij.analysis.AnalysisScope;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Query;
import it.sesalab.brunelleschi.graph_detection.DependencyGraph;
import it.sesalab.brunelleschi.graph_detection.DependencyGraphFactory;
import lombok.RequiredArgsConstructor;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.builder.GraphBuilder;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class JGraphTPsiClassDependencyGraphFactory  extends DependencyGraphFactory {

    protected static final String DEPENDENCY_LABEL = "dependency";
    protected static final String EXTENDS_LABEL = "extends";

    private final Project currentProject;
    private GraphBuilder<PsiClass, LabeledEdge, ? extends DefaultDirectedGraph<PsiClass, LabeledEdge>> graphBuilder;

    @Override
    public DependencyGraph makeDependencyGraph() {
        graphBuilder = DefaultDirectedGraph.createBuilder(LabeledEdge.class);
        for (PsiPackage aPackage : allProjectJavaPackages()) {
            for (PsiClass currentClass : aPackage.getClasses()) {
                graphBuilder.addVertex(currentClass);
                addDependenciesOf(currentClass);
            }
        }
        return new JGraphTPsiClassDependencyGraph(false, graphBuilder.build());
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

    protected Set<PsiPackage> allProjectJavaPackages() {
        final Set<PsiPackage> foundPackages = new HashSet<>();

        AnalysisScope scope = new AnalysisScope(this.currentProject);
        scope.accept(new PsiRecursiveElementVisitor() {
            @Override
            public void visitFile(final PsiFile file) {
                if (file instanceof PsiJavaFile) {
                    PsiJavaFile psiJavaFile = (PsiJavaFile) file;
                    final PsiPackage aPackage =
                            JavaPsiFacade.getInstance(currentProject).findPackage(psiJavaFile.getPackageName());
                    if (aPackage != null) {
                        foundPackages.add(aPackage);
                    }
                }
            }
        });
        return foundPackages;
    }
}
