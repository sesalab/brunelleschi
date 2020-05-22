package it.sesalab.brunelleschi.application.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.ui.Messages;
import it.sesalab.brunelleschi.application.presenters.FindSmellPresenter;
import it.sesalab.brunelleschi.application.configuration.ExperimentalConfiguration;
import it.sesalab.brunelleschi.core.entities.ArchitecturalSmell;
import it.sesalab.brunelleschi.core.entities.detector.SmellDetectorBuilder;
import it.sesalab.brunelleschi.core.usecases.findsmell.FindAllSmellBuildingCommand;
import it.sesalab.brunelleschi.core.usecases.findsmell.FindSmellInteractor;
import it.sesalab.brunelleschi.core.usecases.findsmell.SmellDetectorBuildingCommand;
import it.sesalab.brunelleschi.graph_detection.DependencyGraph;
import it.sesalab.brunelleschi.graph_detection.GraphBasedDetectorBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class FindAllSmellAction extends AnAction {

    private int smellFound;
    private Collection<ArchitecturalSmell> results;
    private ExperimentalConfiguration configuration;

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        ProgressManager.getInstance().runProcessWithProgressSynchronously(() -> {

            ProgressIndicator indicator = ProgressManager.getInstance().getProgressIndicator();
            indicator.setIndeterminate(true);
            indicator.setText("Analyzing project ...");

            ApplicationManager.getApplication().runReadAction(() -> {
                configuration = new ExperimentalConfiguration(e.getProject());
                DependencyGraph packageDependencyGraph = configuration.getPackageGraphFactory().makeDependencyGraph();
                DependencyGraph classDependencyGraph = configuration.getClassDependencyGraphFactory().makeDependencyGraph();
                SmellDetectorBuilder smellDetectorBuilder = new GraphBasedDetectorBuilder(packageDependencyGraph,classDependencyGraph);
                SmellDetectorBuildingCommand command = new FindAllSmellBuildingCommand(smellDetectorBuilder,10);
                FindSmellInteractor interactor = new FindSmellInteractor(command);
                results = interactor.execute();
            });

            FindSmellPresenter presenter = configuration.getPresenter();
            presenter.present(results);
            smellFound = results.size();

        }, "Brunelleschi - Architectural Smell Detection", false, e.getProject());

        Messages.showMessageDialog(e.getProject(), "Brunelleschi found "+smellFound+" smells", "ATTENTION! ", Messages.getInformationIcon());
    }
}
