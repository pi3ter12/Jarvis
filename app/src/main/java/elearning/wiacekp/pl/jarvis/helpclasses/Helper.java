package elearning.wiacekp.pl.jarvis.helpclasses;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import elearning.wiacekp.pl.jarvis.commands.CommandsList;

public class Helper {
    private  Context ctx;

    public Helper(Context ctx){
        this.ctx=ctx;
    }

    public int getIdMusicFiles(ArrayList<FilesList> musicList, File file){
        for(int i=0; i<musicList.size(); i++){
            if(musicList.get(i).getFile().getAbsolutePath().equals(file.getAbsolutePath())){
                return i;
            }
        }
        return -1;
    }

    public boolean searchHelper(String tekst, String str){
        boolean end=false;
        int number=0;

        for(int i=0; i<tekst.length(); i++){
            if(tekst.charAt(i)==str.charAt(0)){
                if(i+str.length()<=tekst.length()){
                    number=1;
                    for(int j=1, k=i+1; j<str.length(); j++, k++){
                        if(tekst.charAt(k)==str.charAt(j)){
                            number++;
                        }
                    }
                }
                if(number==str.length()){
                    i=tekst.length();
                    end=true;
                }
            }

        }
        return end;
    }

    public void saveNewCommand(int position, String command){
        ArrayList<CommandsList> commandsLists = new ArrayList<>();
        commandsLists=getCommandList();

        commandsLists.get(position).setCommand(command);
        JSONArray jsonArray = new JSONArray();
        for(int i=0; i<commandsLists.size(); i++){
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("id", commandsLists.get(i).getId());
                jsonObject.put("commandNumber", commandsLists.get(i).getCommandNumber());
                jsonObject.put("command", commandsLists.get(i).getCommand());
                jsonObject.put("approximately", commandsLists.get(i).isApproximately());
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        saveFile(jsonArray.toString());
    }

    public String readFile(){
        String out="";

        try {
            File file = new File(ctx.getFilesDir().getAbsolutePath()+"/comendy.json");
            Scanner in = new Scanner(file);
            do{
                out+=in.nextLine();
            }while (in.hasNextLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return out;
    }

    public void saveFile(String json){
        try {
            File file = new File(ctx.getFilesDir().getAbsolutePath()+"/comendy.json");
            PrintWriter pw = new PrintWriter(file);
            pw.print(json);
            pw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<CommandsList> getCommandList(){
        ArrayList<CommandsList>commandsList = new ArrayList<>();
        try {
            String json = readFile();
            if(json.equals("")) {
                Scanner in = new Scanner(ctx.getAssets().open("commands.json"));
                do {
                    json += in.nextLine();
                } while (in.hasNextLine());
                saveFile(json);
            }

            JSONArray jsonArray = new JSONArray(json);
            for(int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                commandsList.add(
                        new CommandsList(jsonObject.getInt("id"),
                                jsonObject.getInt("commandNumber"),
                                jsonObject.getString("command"),
                                jsonObject.getBoolean("approximately")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e){
            e.printStackTrace();
        }
        return commandsList;
    }
}
