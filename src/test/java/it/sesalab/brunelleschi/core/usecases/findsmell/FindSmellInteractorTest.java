package it.sesalab.brunelleschi.core.usecases.findsmell;

import it.sesalab.brunelleschi.core.entities.ArchitecturalSmell;
import it.sesalab.brunelleschi.core.entities.detector.SmellDetector;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class FindSmellInteractorTest {

    @Mock
    SmellDetector mockDetector;

    @Mock
    SmellDetectorBuildingCommand mockCommand;

    @Mock
    ArchitecturalSmell mockSmell;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mockCommand.buildSmellDetector()).thenReturn(mockDetector);
        when(mockDetector.detectSmells()).thenReturn(Collections.singletonList(mockSmell));
    }

    @Test
    public void testInteractorCallsDependenciesCorrectly() {
        FindSmellInteractor interactor = new FindSmellInteractor(mockCommand);

        Collection<ArchitecturalSmell> architecturalSmells = interactor.execute();

        verify(mockCommand,times((1))).buildSmellDetector();
        verify(mockDetector,times(1)).detectSmells();
        assertThat(architecturalSmells.size()).isEqualTo(1);
    }
}