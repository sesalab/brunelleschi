package it.sesalab.brunelleschi.graph_detection.jgrapht;

import com.intellij.analysis.AnalysisScope;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.Query;
import it.sesalab.brunelleschi.graph_detection.DependencyGraph;
import it.sesalab.brunelleschi.graph_detection.DependencyGraphFactory;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;


public abstract class JGraphTPsiDependencyGraphFactory extends DependencyGraphFactory {

    protected final Project currentProject;

    protected JGraphTPsiDependencyGraphFactory(Project currentProject) {
        this.currentProject = currentProject;
    }

    @Override
    public abstract DependencyGraph makeDependencyGraph();

    @NotNull
    protected Set<PsiClass> findDependentClasses(PsiClass currentClass) {
        Query<PsiReference> search = ReferencesSearch.search(currentClass);

        Set<PsiClass> result = new HashSet<>();
        for (PsiReference ref : search.findAll()) {
            PsiClass dependentClass = PsiTreeUtil.getParentOfType(ref.getElement(), PsiClass.class);
            if(dependentClass != null) {
                result.add(dependentClass);
            }

        }
        return result;
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
