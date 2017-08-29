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

    OutputConverterComponent outputConverterComponent;
    DefaultHostComponent defaultHostComponent;
    SplitCommandComponent splitCommandComponent;
    CliService cliService;

    @Before
    public void prepare() {
        outputConverterComponent = Mockito.mock(OutputConverterComponent.class);
        when(outputConverterComponent.stringify("OK")).thenReturn("OK");
        defaultHostComponent = Mockito.mock(DefaultHostComponent.class);
        splitCommandComponent = new SplitCommandComponent();
        cliService = new CliService(outputConverterComponent,defaultHostComponent,splitCommandComponent);
    }


    @Test
    public void testConnectionError() {
        String result = cliService.executeCommand("asd",1234,"SET FOO BAR");
        assertThat(result).as("Check connection error").isEqualTo("ERR failed to connect to specified hostname and port");
    }

}
