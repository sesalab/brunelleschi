package it.sesalab.brunelleschi.graph;

import com.intellij.analysis.AnalysisScope;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import lombok.Data;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Abstract class for a Project Dependency Graph
 */
@Data
public abstract class DependencyGraph {
    protected static final String DEPENDENCY_LABEL = "dependency";
    protected static final String EXTENDS_LABEL = "extends";
    protected static final Logger log = Logger.getLogger(JGraphTDependencyGraph.class);

    protected final Project currentProject;

    protected abstract void buildClassDependencyGraph();

    public Project getCurrentProject() {
        return this.currentProject;
    }

    public abstract Map<PsiClass, List<PsiClass>> adjacencyList();

    public abstract Set<Set<PsiClass>> getCycles();

    protected Set<PsiPackage> allProjectJavaPackages() {
        final Set<PsiPackage> foundPackages = new HashSet<>();

        AnalysisScope scope = new AnalysisScope(this.currentProject);
        scope.accept(new PsiRecursiveElementVisitor() {
            @Override
            public void visitFile(final PsiFile file) {
                if (file instanceof PsiJavaFile) {
                    PsiJavaFile psiJavaFile = (PsiJavaFile) file;
                    final PsiPackage aPackage =
                            JavaPsiFacade.getInstance(getCurrentProject()).findPackage(psiJavaFile.getPackageName());
                    if (aPackage != null) {
                        foundPackages.add(aPackage);
                        //logger.severe("found " + aPackage.getQualifiedName());//aggiungi logger
                    }
                }
            }
        });
        return foundPackages;
    }

}
