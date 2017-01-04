package elearning.wiacekp.pl.jarvis.commands;

public class CommandsList {
    private int id;
    private int commandNumber;
    private String command;
    private boolean approximately;

    public CommandsList(int id, int commandNumber, String command, boolean approximately) {
        this.id = id;
        this.commandNumber = commandNumber;
        this.command = command;
        this.approximately = approximately;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommandNumber() {
        return commandNumber;
    }

    public void setCommandNumber(int commandNumber) {
        this.commandNumber = commandNumber;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean isApproximately() {
        return approximately;
    }

    public void setApproximately(boolean approximately) {
        this.approximately = approximately;
    }
}
