package it.sesalab.brunelleschi.graph_detection.jgrapht;

import com.intellij.analysis.AnalysisScope;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import lombok.RequiredArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class PsiPackageExtractor {

    private final Project currentProject;

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