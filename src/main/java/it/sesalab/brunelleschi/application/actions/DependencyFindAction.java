package it.sesalab.brunelleschi.application.actions;

import com.intellij.analysis.AnalysisScope;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.packageDependencies.ForwardDependenciesBuilder;
import com.intellij.psi.*;
import com.intellij.psi.search.searches.ReferencesSearch;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtil;
import com.intellij.util.Query;
import com.mxgraph.layout.mxCircleLayout;
import com.mxgraph.layout.mxIGraphLayout;
import com.mxgraph.util.mxCellRenderer;
import it.sesalab.brunelleschi.graph_detection.jgrapht.LabeledEdge;
import org.jgrapht.Graph;
import org.jgrapht.ext.JGraphXAdapter;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DependencyFindAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent event) {
        System.out.println("Action Started...");
        Graph<PsiClass, DefaultEdge> dependencyGraph = new DefaultDirectedGraph<>(DefaultEdge.class);
        Set<PsiPackage> allProjectJavaPackages = allProjectJavaPackages(event.getProject());
        List<PsiClass> projectClasses = allProjectJavaPackages.stream().flatMap(psiPackage1 -> Stream.of(psiPackage1.getClasses())).collect(Collectors.toList());
        for(PsiClass psiClass: projectClasses){
            dependencyGraph.addVertex(psiClass);
            ForwardDependenciesBuilder.analyzeFileDependencies(psiClass.getContainingFile(),(place, dependency) -> {
                if(dependency instanceof PsiClass && projectClasses.contains(dependency)){
                    PsiClass dependency1 = (PsiClass) dependency;
                    dependencyGraph.addVertex(dependency1);
                    dependencyGraph.addEdge(psiClass, dependency1);
                }
            });
        }

        String folderName = System.getProperty("user.home") + File.separator + ".brunelleschi" + File.separator + event.getProject().getName();
        String imgName = folderName + File.separator + "graph.png";
        JGraphXAdapter<PsiClass, DefaultEdge> graphAdapter = new JGraphXAdapter<>(dependencyGraph);
        mxIGraphLayout layout = new mxCircleLayout(graphAdapter);
        layout.execute(graphAdapter.getDefaultParent());

        try {
            BufferedImage image =
                    mxCellRenderer.createBufferedImage(graphAdapter, null, 2, Color.WHITE, true, null);
            File folder = new File(folderName);
            if(!folder.exists()){
                folder.mkdirs();
            }
            File imgFile = new File(imgName);
            if(!imgFile.exists()){
                imgFile.createNewFile();
            }
            ImageIO.write(image, "PNG", imgFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Action Ended...");
    }

    protected Set<PsiClass> findDependentClasses(PsiClass currentClass) {
        Query<PsiReference> search = ReferencesSearch.search(currentClass);

        Set<PsiClass> result = new HashSet<>();
        for (PsiReference ref : search.findAll()) {
            PsiClass dependentClass = PsiTreeUtil.getParentOfType(ref.getElement(), PsiClass.class);
            if(dependentClass != null && classIsLegit(dependentClass)) {
                result.add(dependentClass);
            }

        }
        return result;
    }

    protected boolean classIsLegit(PsiClass psiClass){
        return !(psiClass.isAnnotationType() || psiClass.isEnum());
    }

    protected Set<PsiPackage> allProjectJavaPackages(Project currentProject) {
        final Set<PsiPackage> foundPackages = new HashSet<>();

        AnalysisScope scope = new AnalysisScope(currentProject);
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
