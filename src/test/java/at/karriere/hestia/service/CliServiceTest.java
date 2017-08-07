package at.karriere.hestia.service;


import at.karriere.hestia.component.DefaultHostComponent;
import at.karriere.hestia.component.OutputConverterComponent;
import at.karriere.hestia.repository.CliRepository;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class CliServiceTest {

    CliRepository cliRepository;
    OutputConverterComponent outputConverterComponent;
    DefaultHostComponent defaultHostComponent;

    CliService cliService;

    @Before
    public void prepare() {
        cliRepository = Mockito.mock(CliRepository.class);
        Mockito.when(cliRepository.readResult()).thenReturn("OK");
        Mockito.when(cliRepository.connect("localhost",1234)).thenReturn(true);
        outputConverterComponent = Mockito.mock(OutputConverterComponent.class);
        Mockito.when(outputConverterComponent.stringify("OK")).thenReturn("OK");
        defaultHostComponent = Mockito.mock(DefaultHostComponent.class);

        cliService = new CliService(cliRepository,outputConverterComponent,defaultHostComponent);
    }


    @Test
    public void testValidCommand() {
        String result = cliService.executeCommand("localhost",1234,"SET","FOO", "BAR");
        Assertions.assertThat(result).as("Check valid command").isEqualTo("OK");
    }

    @Test
    public void testInvalidCommand() {
        String result = cliService.executeCommand("localhost", 1234, "asd", "FOO", "BAR");
        Assertions.assertThat(result).as("Check invalid command").isEqualTo("ERR illegal command 'asd'");
    }
}
