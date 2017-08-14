package at.karriere.hestia;

import at.karriere.hestia.component.DBCommandWrapperComponent;
import at.karriere.hestia.component.DefaultHostComponent;
import at.karriere.hestia.component.OutputConverterComponent;
import at.karriere.hestia.component.SplitCommandComponent;
import at.karriere.hestia.repository.CliRepository;
import at.karriere.hestia.service.CliService;
import at.karriere.hestia.service.DBWrapperCliService;
import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.*;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;

public class DockerTestListener extends RunListener {

    @Override
    public void testStarted(Description description) throws Exception {
        DockerHandler.getInstance().start();
        super.testStarted(description);
    }

    @Override
    public void testRunFinished(Result result) throws Exception {
        DockerHandler.getInstance().stop();
        super.testRunFinished(result);
    }

    public DockerTestListener() {

    }



}
