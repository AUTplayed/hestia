package at.karriere.hestia.controller;

import at.karriere.hestia.service.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class ApiControllerTest {

    ApiController apiController;

    @Before
    public void prepare() {
        KeysService keysService = Mockito.mock(KeysService.class);
        KeyspaceService keyspaceService = Mockito.mock(KeyspaceService.class);
        NamespaceScheduleService namespaceScheduleService = Mockito.mock(NamespaceScheduleService.class);
        NamespaceService namespaceService = Mockito.mock(NamespaceService.class);
        InfoService infoService = Mockito.mock(InfoService.class);
        ExportService exportService = Mockito.mock(ExportService.class);
        ExactKeysService exactKeysService = Mockito.mock(ExactKeysService.class);

        when(keysService.keysJson(any(),any(),any(),any(),any(), any())).thenReturn("OK");
        when(keyspaceService.getKeySpacesJson(any(),any())).thenReturn("OK");
        when(namespaceService.getNamespaces(any(),any(),any())).thenReturn("OK");
        when(infoService.getInfo(any(),any())).thenReturn("OK");
        when(exportService.export(any(),any(),any(),any(),any())).thenReturn("OK");

        apiController = new ApiController(keysService, keyspaceService, namespaceScheduleService, namespaceService, infoService, exportService, exactKeysService);
    }


    @Test
    public void increaseTestCoverage() throws UnsupportedEncodingException {
        assertThat(apiController.keys(null, null, null, null, null, null)).isEqualTo("OK");
        assertThat(apiController.keyspaces(null, null)).isEqualTo("OK");
        assertThat(apiController.scheduleNamespaces(null, null)).isEqualTo("Done");
        assertThat(apiController.namespaces(null, null, null)).isEqualTo("OK");
        assertThat(apiController.info(null, null)).isEqualTo("OK");
        assertThat(apiController.export(null, null, null, null, "")).isEqualTo("OK");


    }

}
