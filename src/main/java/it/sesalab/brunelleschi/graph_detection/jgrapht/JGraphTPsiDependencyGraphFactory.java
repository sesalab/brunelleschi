package it.sesalab.brunelleschi.graph_detection.jgrapht;

import com.intellij.analysis.AnalysisScope;
import com.intellij.openapi.project.Project;
import com.intellij.packageDependencies.ForwardDependenciesBuilder;
import com.intellij.psi.*;
import it.sesalab.brunelleschi.graph_detection.DependencyGraph;
import it.sesalab.brunelleschi.graph_detection.DependencyGraphFactory;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public abstract class JGraphTPsiDependencyGraphFactory extends DependencyGraphFactory {

    protected final Project currentProject;
    protected final Set<PsiClass> projectClasses;

    protected JGraphTPsiDependencyGraphFactory(Project currentProject) {
        this.currentProject = currentProject;
        this.projectClasses = allProjectJavaPackages().stream().flatMap(psiPackage1 -> Stream.of(psiPackage1.getClasses())).collect(Collectors.toSet());
    }

    @Override
    public abstract DependencyGraph makeDependencyGraph();

    @NotNull
    protected Set<PsiClass> findDependentClasses(PsiClass currentClass) {
        Set<PsiClass> result = new HashSet<>();
        ForwardDependenciesBuilder.analyzeFileDependencies(currentClass.getContainingFile(),(place, dependency) -> {
            if(classIsLegit(dependency)){
                PsiClass dependentClass = (PsiClass) dependency;
                result.add(dependentClass);
            }
        });
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

    protected boolean classIsLegit(PsiElement dependency){
        return dependency instanceof PsiClass && projectClasses.contains(dependency) && !(((PsiClass) dependency).isAnnotationType() || ((PsiClass) dependency).isEnum());
    }
}
