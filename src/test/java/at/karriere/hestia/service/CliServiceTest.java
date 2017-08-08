package at.karriere.hestia.service;


import at.karriere.hestia.component.DefaultHostComponent;
import at.karriere.hestia.component.OutputConverterComponent;
import at.karriere.hestia.component.SplitCommandComponent;
import at.karriere.hestia.repository.CliRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

public class CliServiceTest {

    CliRepository cliRepository;
    OutputConverterComponent outputConverterComponent;
    DefaultHostComponent defaultHostComponent;
    SplitCommandComponent splitCommandComponent;
    CliService cliService;

    @Before
    public void prepare() {
        cliRepository = Mockito.mock(CliRepository.class);
        when(cliRepository.readResult()).thenReturn("OK");
        when(cliRepository.connect("localhost",1234)).thenReturn(true);
        outputConverterComponent = Mockito.mock(OutputConverterComponent.class);
        when(outputConverterComponent.stringify("OK")).thenReturn("OK");
        defaultHostComponent = Mockito.mock(DefaultHostComponent.class);
        splitCommandComponent = new SplitCommandComponent();
        cliService = new CliService(cliRepository,outputConverterComponent,defaultHostComponent,splitCommandComponent);
    }


    @Test
    public void testValidCommand() {
        String result = cliService.executeCommand("localhost",1234,"SET FOO BAR");
        assertThat(result).as("Check valid command").isEqualTo("OK");
    }

    @Test
    public void testInvalidCommand() {
        String result = cliService.executeCommand("localhost", 1234, "asd FOO BAR");
        assertThat(result).as("Check invalid command").isEqualTo("ERR illegal command 'asd'");
    }
}
