package it.sesalab.brunelleschi.application.configuration;

import com.intellij.openapi.project.Project;
import it.sesalab.brunelleschi.application.presenters.CsvFindSmellPresenter;
import it.sesalab.brunelleschi.application.presenters.FindSmellPresenter;
import it.sesalab.brunelleschi.application.presenters.FindSmellSummaryPresenter;
import it.sesalab.brunelleschi.graph_detection.DependencyGraphFactory;
import it.sesalab.brunelleschi.graph_detection.jgrapht.JGraphTPsiClassDependencyGraphFactory;
import it.sesalab.brunelleschi.graph_detection.jgrapht.JGraphTPsiPackageDependencyGraphFactory;

/**
 * This class represent the configuration used for
 * the first experiments. Detectors are all graph based
 * and the only command provided is to find all smells.
 * There is no GUI. All results are exported in a csv file
 * at $USER_HOME$/.brunelleschi/projectName/timestamp-results.csv
 */
public class ExperimentalConfiguration {

    private final Project currentProject;
    private final DependencyGraphFactory classDependencyGraphFactory;
    private final DependencyGraphFactory packageGraphFactory;
    private final FindSmellPresenter presenter;


    public ExperimentalConfiguration(Project currentProject) {
        this.currentProject = currentProject;
        packageGraphFactory = new JGraphTPsiPackageDependencyGraphFactory(currentProject);
        classDependencyGraphFactory = new JGraphTPsiClassDependencyGraphFactory(currentProject);
        CsvFindSmellPresenter basePresenter = new CsvFindSmellPresenter(currentProject.getName());
        presenter = new FindSmellSummaryPresenter(basePresenter,currentProject.getName());
    }


    public DependencyGraphFactory getClassDependencyGraphFactory() {
        return classDependencyGraphFactory;
    }

    public DependencyGraphFactory getPackageGraphFactory() {
        return packageGraphFactory;
    }

    public FindSmellPresenter getPresenter() {
        return presenter;
    }
}
