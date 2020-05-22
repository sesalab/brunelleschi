package it.sesalab.brunelleschi.application.presenters;

import it.sesalab.brunelleschi.core.entities.ArchitecturalSmell;
import it.sesalab.brunelleschi.core.entities.Component;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class CsvFindSmellPresenter implements FindSmellPresenter {
    //TODO: TEST!!!!
    enum Headers {
        ProjectName,
        SmellName,
        ComponentName,
        ComponentType
    }


    private final String projectName;
    private File baseDirectory;

    public CsvFindSmellPresenter(String projectName) {
        this.projectName = projectName;
        baseDirectory = new File(System.getProperty("user.home") + File.separator + ".brunelleschi" + File.separator + projectName);
    }


    @Override
    public void present(Collection<ArchitecturalSmell> smells) {
        try {
            if(!baseDirectory.exists()){
                baseDirectory.mkdirs();
            }
            int counter = 0;
            for(ArchitecturalSmell smell: smells){
                File outputFile = new File(baseDirectory.getAbsolutePath() + File.separator + smell.getSmellType()+"-"+counter+".csv");
                CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(outputFile), CSVFormat.DEFAULT.withHeader(Headers.class));
                for(Component component: smell.getAffectedComponents()){
                    csvPrinter.printRecord(projectName,smell.getSmellType(),component.getQualifiedName(),component.getType());
                }
                csvPrinter.close(true);
                counter++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
