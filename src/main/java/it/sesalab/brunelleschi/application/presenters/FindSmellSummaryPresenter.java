package it.sesalab.brunelleschi.application.presenters;

import it.sesalab.brunelleschi.core.entities.ArchitecturalSmell;
import it.sesalab.brunelleschi.core.entities.SmellType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class FindSmellSummaryPresenter extends AbstractPresenterDecorator {

    enum Headers {
        ProjectName,
        TotalSmells,
        CyclicDependencies,
        HubLikes,
        Unstable
    }

    private final String projectName;
    private int nOfCyclic;
    private int nOfHubLikes;
    private int nOfUnstable;

    public FindSmellSummaryPresenter(FindSmellPresenter wrappedPresenter, String projectName) {
        super(wrappedPresenter);
        this.projectName = projectName;
        nOfCyclic = 0;
        nOfUnstable = 0;
        nOfHubLikes = 0;
    }

    @Override
    public void present(Collection<ArchitecturalSmell> smells) {
        wrappedPresenter.present(smells);
        try (CSVPrinter csvPrinter = getCsvPrinter()) {
            countSmellOccurrences(smells);
            csvPrinter.printRecord(projectName,smells.size(),nOfCyclic,nOfHubLikes,nOfUnstable);
            csvPrinter.close(true);
        } catch (IOException e){
            e.printStackTrace();
        }

    }

    private void countSmellOccurrences(Collection<ArchitecturalSmell> smells) {
        for (ArchitecturalSmell smell: smells){
            switch (smell.getSmellType()){

                case CYCLIC_DEPENDENCY:
                    nOfCyclic++;
                    break;
                case HUB_LIKE_DEPENDENCY :
                case BORDERLINE_HUB_LIKE:
                    nOfHubLikes++;
                    break;
                case UNSTABLE_DEPENDENCY:
                    nOfUnstable++;
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + smell.getSmellType());
            }

        }
    }

    @NotNull
    private CSVPrinter getCsvPrinter() throws IOException {
        File baseDirectory = new File(System.getProperty("user.home") + File.separator + ".brunelleschi" + File.separator + projectName);
        if(!baseDirectory.exists()){
            baseDirectory.mkdirs();
        }
        File outputFile = new File(baseDirectory.getAbsolutePath() + File.separator  + "summary.csv");
        return new CSVPrinter(new FileWriter(outputFile), CSVFormat.DEFAULT.withHeader(Headers.class));
    }
}
