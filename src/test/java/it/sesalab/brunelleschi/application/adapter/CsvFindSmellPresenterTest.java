package it.sesalab.brunelleschi.application.adapter;

import it.sesalab.brunelleschi.core.entities.ArchitecturalSmell;
import it.sesalab.brunelleschi.core.entities.Component;
import it.sesalab.brunelleschi.core.entities.ComponentType;
import it.sesalab.brunelleschi.core.entities.SmellType;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class CsvFindSmellPresenterTest {

    public static final String ORACLE_FILE_NAME = "oracle.csv";
    public static final String ACTUAL_FILE_NAME = "actual.csv";
    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    @Before
    public void setUp() throws Exception {
        folder.delete();
        folder.create();
    }

    @Test
    public void whenOneSmellWithOneComponentItShouldWrite2Lines() throws IOException {

        ArchitecturalSmell inputSmell = new ArchitecturalSmell(SmellType.HUB_LIKE_DEPENDENCY);
        inputSmell.addAffectedComponent(new Component("test.A", ComponentType.CLASS,true));

        File oracleFile = folder.newFile(ORACLE_FILE_NAME);
        CSVPrinter printer = getCsvPrinter(oracleFile);
        printer.printRecord("HUB_LIKE_DEPENDENCY", "test.A", "CLASS");
        printer.close(true);

        File actualFile = folder.newFile(ACTUAL_FILE_NAME);
        CsvFindSmellPresenter presenter = new CsvFindSmellPresenter(actualFile);
        presenter.present(Collections.singleton(inputSmell));


        assertThat(FileUtils.contentEquals(oracleFile,actualFile)).isTrue();
    }

    @Test
    public void whenCyclicDependencyShouldWriteAtLeast3Lines() throws IOException {
        ArchitecturalSmell inputSmell = new ArchitecturalSmell(SmellType.CYCLIC_DEPENDENCY);
        inputSmell.addAffectedComponent(new Component("test.A", ComponentType.CLASS,true));
        inputSmell.addAffectedComponent(new Component("test.B", ComponentType.CLASS,true));

        File oracleFile = folder.newFile(ORACLE_FILE_NAME);
        CSVPrinter printer = getCsvPrinter(oracleFile);
        printer.printRecord("CYCLIC_DEPENDENCY", "test.A", "CLASS");
        printer.printRecord("CYCLIC_DEPENDENCY", "test.B", "CLASS");
        printer.close();

        File actualFile = folder.newFile(ACTUAL_FILE_NAME);
        CsvFindSmellPresenter presenter = new CsvFindSmellPresenter(actualFile);
        presenter.present(Collections.singleton(inputSmell));


        Reader in = new FileReader(actualFile);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
        for (CSVRecord record : records) {
            System.out.println(record.toString());
        }

        assertThat(FileUtils.contentEquals(oracleFile,actualFile)).isTrue();


    }

    @Test
    public void whenMoreThanOneSmellShouldWriteAtLeast3Lines() throws IOException {
        List<ArchitecturalSmell> inputSmell = new ArrayList<>();
        ArchitecturalSmell hublike = new ArchitecturalSmell(SmellType.HUB_LIKE_DEPENDENCY);
        hublike.addAffectedComponent(new Component("test.A", ComponentType.CLASS,true));
        inputSmell.add(hublike);

        ArchitecturalSmell cyclic = new ArchitecturalSmell(SmellType.CYCLIC_DEPENDENCY);
        cyclic.addAffectedComponent(new Component("test.A", ComponentType.CLASS,true));
        cyclic.addAffectedComponent(new Component("test.B", ComponentType.CLASS,true));
        inputSmell.add(cyclic);

        File oracleFile = folder.newFile(ORACLE_FILE_NAME);
        CSVPrinter printer = getCsvPrinter(oracleFile);
        printer.printRecord("HUB_LIKE_DEPENDENCY", "test.A", "CLASS");
        printer.printRecord("CYCLIC_DEPENDENCY", "test.A", "CLASS");
        printer.printRecord("CYCLIC_DEPENDENCY", "test.B", "CLASS");
        printer.close(true);

        File actualFile = folder.newFile(ACTUAL_FILE_NAME);
        CsvFindSmellPresenter presenter = new CsvFindSmellPresenter(actualFile);
        presenter.present(inputSmell);

       assertThat(FileUtils.contentEquals(oracleFile,actualFile)).isTrue();

//        Reader in = new FileReader(actualFile);
//        Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(in);
//        for (CSVRecord record : records) {
//            System.out.println(record.toString());
//        }
    }

    @Test
    public void whenNoSmellShouldWriteOnlyHeaders() throws IOException {
        File oracleFile = folder.newFile(ORACLE_FILE_NAME);
        CSVPrinter csvPrinter = getCsvPrinter(oracleFile);
        csvPrinter.close(true);

        File actualFile = folder.newFile(ACTUAL_FILE_NAME);
        CsvFindSmellPresenter presenter = new CsvFindSmellPresenter(actualFile);
        presenter.present(Collections.emptyList());

        assertThat(FileUtils.contentEquals(oracleFile,actualFile)).isTrue();
    }

    @NotNull
    private CSVPrinter getCsvPrinter(File oracleFile) throws IOException {
        FileWriter out = new FileWriter(oracleFile);
        CSVFormat format = CSVFormat.DEFAULT.withHeader("SmellName", "ComponentName", "ComponentType");
        return new CSVPrinter(out, format);
    }
}