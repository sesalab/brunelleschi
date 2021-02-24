package it.sesalab.brunelleschi.graph_detection.jgrapht;

import com.intellij.openapi.project.Project;
import com.intellij.psi.JavaPsiFacade;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiPackage;
import com.intellij.psi.util.PsiUtil;
import it.sesalab.brunelleschi.graph_detection.DependencyGraph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.builder.GraphBuilder;


public class JGraphTPsiPackageDependencyGraphFactory extends JGraphTPsiDependencyGraphFactory {

    private GraphBuilder<PsiPackage, LabeledEdge, ? extends DefaultDirectedGraph<PsiPackage, LabeledEdge>> graphBuilder;
    private JavaPsiFacade javaPsiFacade;

    public JGraphTPsiPackageDependencyGraphFactory(Project currentProject) {
        super(currentProject);
        this.javaPsiFacade = JavaPsiFacade.getInstance(this.currentProject);
    }

    @Override
    public DependencyGraph makeDependencyGraph() {
        graphBuilder = DefaultDirectedGraph.createBuilder(LabeledEdge.class);

        for (PsiPackage currentPackage : allProjectJavaPackages()) {
            graphBuilder.addVertex(currentPackage);
            for (PsiClass currentClass : currentPackage.getClasses()) {

                for (PsiClass dependentClass: findDependentClasses(currentClass)) {

                    if(!areClassesInTheSamePackage(currentClass, dependentClass)){
                        PsiPackage dependentPackage = javaPsiFacade.findPackage(PsiUtil.getPackageName(dependentClass));
                        graphBuilder.addVertex(dependentPackage);
                        makeEdgeBetween(currentPackage, dependentPackage);
                    }
                }
            }
        }
        return new JGraphTPsiDependencyGraph<>(true, graphBuilder.build(), currentProject.getName());
    }

    private boolean areClassesInTheSamePackage(PsiClass currentClass, PsiClass dependentClass) {
        return javaPsiFacade.arePackagesTheSame(currentClass, dependentClass);
    }

    private void makeEdgeBetween(PsiPackage currentPackage, PsiPackage dependentPackage) {
        LabeledEdge edge = new LabeledEdge(DEPENDENCY_LABEL);
        graphBuilder.addEdge(currentPackage, dependentPackage, edge);
    }

}
