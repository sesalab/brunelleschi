package it.sesalab.brunelleschi.core.usecases.findsmell;

import it.sesalab.brunelleschi.core.entities.detector.SmellDetector;
import it.sesalab.brunelleschi.core.entities.detector.SmellDetectorBuilder;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class FindAllSmellBuildingCommandTest {

    FindAllSmellBuildingCommand commandUnderTest;

    @Mock
    SmellDetectorBuilder mockBuilder;

    @Mock
    SmellDetector mockDetector;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mockBuilder.enableCyclicDependencyDetection()).thenReturn(mockBuilder);
        when(mockBuilder.enableHubLikeDependencyDetection(anyInt())).thenReturn(mockBuilder);
        when(mockBuilder.enableUnstableDependencyDetection()).thenReturn(mockBuilder);
        when(mockBuilder.build()).thenReturn(mockDetector);
    }

    @Test
    public void testCommandInvokesBuilderCorrectly() {
        int hubLikeThreshold = 0;
        double unstableDependencyThreshold = 0.0;
        commandUnderTest = new FindAllSmellBuildingCommand(mockBuilder, hubLikeThreshold, unstableDependencyThreshold);

        commandUnderTest.buildSmellDetector();
        verify(mockBuilder,times(1)).enableCyclicDependencyDetection();
        verify(mockBuilder,times(1)).enableHubLikeDependencyDetection(hubLikeThreshold);
        verify(mockBuilder,times(1)).enableUnstableDependencyDetection();
        verify(mockBuilder,times(1)).build();
    }
}