package it.sesalab.brunelleschi.application.adapter;

import it.sesalab.brunelleschi.core.entities.ArchitecturalSmell;
import it.sesalab.brunelleschi.core.entities.Component;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class CsvFindSmellPresenter implements FindSmellPresenter {

    enum Headers {
        SmellName,
        ComponentName,
        ComponentType
    }

    private final File outputFile;

    public CsvFindSmellPresenter(File outputFile) {
        this.outputFile = outputFile;
    }

    @Override
    public void present(Collection<ArchitecturalSmell> smells) {
        try {
            CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(outputFile), CSVFormat.DEFAULT.withHeader(Headers.class));
            for(ArchitecturalSmell smell: smells){
                for(Component component: smell.getAffectedComponents()){
                    csvPrinter.printRecord(smell.getSmellType(),component.getQualifiedName(),component.getType());
                }
            }
            csvPrinter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
