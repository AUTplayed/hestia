package at.karriere.hestia.entity;

import javax.faces.bean.ManagedBean;

@ManagedBean
public class CommandContainer {

    String command;
    String[] args;

    public CommandContainer(String command, String[] args) {
        this.command = command;
        this.args = args;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }
}
