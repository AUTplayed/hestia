package at.karriere.hestia;

import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.DockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.*;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DockerHandler {

    private static DockerHandler instance;
    private boolean running = false;
    private DockerClient docker;
    private Integer redisPort = 6379;
    private String dockerId;

    public static DockerHandler getInstance() {
        if(instance == null) instance = new DockerHandler();
        return instance;
    }

    private DockerHandler() {
        docker = DefaultDockerClient.builder().uri(URI.create("unix:///var/run/docker.sock")).build();
    }

    public void start() {
        if(!running) {
            startDockerContainer();
            running = true;
        }
    }

    public void stop() {
        if(running) {
            stopDockerContainer();
            running = false;
        }
    }

    private void startDockerContainer() {
        try {
            //Pull redis image
            //docker.pull("redis");
            Process pull = Runtime.getRuntime().exec("docker pull redis");
            pull.waitFor();

            //Bind container port 6379(redis default) to 6379 host port
            Map<String, List<PortBinding>> portBindings = new HashMap<>();
            List<PortBinding> binding = new ArrayList<>();
            binding.add(PortBinding.of("0.0.0.0",redisPort));
            portBindings.put("6379",binding);

            //Build host config
            HostConfig hostConfig = HostConfig.builder().portBindings(portBindings).build();

            //Create container config
            ContainerConfig containerConfig = ContainerConfig.builder()
                    .hostConfig(hostConfig)
                    .image("redis").exposedPorts("6379")
                    .build();

            //Create docker
            ContainerCreation creation = docker.createContainer(containerConfig);
            //Get docker id
            dockerId = creation.id();

            //Start container
            docker.startContainer(dockerId);

            ContainerInfo containerInfo = docker.inspectContainer(dockerId);
        } catch (DockerException | InterruptedException | IOException e) {
            e.printStackTrace();
        }
    }

    private void stopDockerContainer() {
        try {
            //Kill and remove docker container
            docker.killContainer(dockerId);
            docker.removeContainer(dockerId);
        } catch (DockerException | InterruptedException e) {
            e.printStackTrace();
        }
    }

}
