package elearning.wiacekp.pl.jarvis.commands;

import android.content.Context;

import java.util.ArrayList;

import elearning.wiacekp.pl.jarvis.helpclasses.Helper;

public class Commands {
    private ArrayList<CommandsList> commandsList = new ArrayList<>();
    private int lastNumber = -1;
    private String phoneNumber = "";
    private String secondPart = "";
    private Context ctx;

    public Commands(Context ctx){
        this.ctx=ctx;
    }

    public int checkCommands(String text){
        commandsList = new Helper(ctx).getCommandList();
        for (int i = 0; i < commandsList.size(); i++) {
            if(commandsList.get(i).isApproximately()){
                if(new Helper(ctx).searchHelper(text, commandsList.get(i).getCommand())){
                    genLastNumber(text);
                    if(commandsList.get(i).getCommandNumber()==21){
                        genPhoneNumber(text, commandsList.get(i).getCommand());
                    }
                    if(commandsList.get(i).getCommandNumber()==27){
                        secondPart = text.replace(commandsList.get(i).getCommand(),"");
                    }
                    return commandsList.get(i).getCommandNumber();
                }
            }else{
                if(commandsList.get(i).getCommand().equals(text)){
                    return commandsList.get(i).getCommandNumber();
                }
            }
        }
        return -1;
    }

    public void genPhoneNumber(String tekst, String command){
        String out = tekst;
        out = out.replace(command, "");
        out = out.replaceAll(" ", "");
        phoneNumber = out;
    }

    public String getSecondPart(){
        return secondPart;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    private void genLastNumber(String text){
        try {
            lastNumber = Integer.parseInt(text.split(" ")[text.split(" ").length - 1]);
        }catch (Exception e){
            lastNumber=-1;
            e.printStackTrace();
        }
    }

    public int getLastNumber(){
        return lastNumber;
    }


}
