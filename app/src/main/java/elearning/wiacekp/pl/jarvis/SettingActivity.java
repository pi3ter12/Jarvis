package elearning.wiacekp.pl.jarvis;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

import elearning.wiacekp.pl.jarvis.commands.CommandsList;
import elearning.wiacekp.pl.jarvis.helpclasses.Helper;

public class SettingActivity extends AppCompatActivity {
    private ArrayList<CommandsList> commandsList = new ArrayList<>();
    private int lastPosition=0;
    private Helper helper=null;
    private ListView lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        helper = new Helper(this);

        lista = (ListView) findViewById(R.id.activity_setting_listview);

        invalidateListView();

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lastPosition=position;
                showDialog();
            }
        });
    }


    public void invalidateListView(){
        commandsList = helper.getCommandList();
        ArrayList<String> toList = new ArrayList<>();

        for(int i=0; i<commandsList.size(); i++){
            toList.add(commandsList.get(i).getCommand());
        }

        ArrayAdapter adapter_listy = new ArrayAdapter(this,
                android.R.layout.simple_list_item_1, toList);
        lista.setAdapter(adapter_listy);
    }

    public void showDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText input = new EditText(this);
        input.setText(commandsList.get(lastPosition).getCommand());
        builder.setMessage("Zmień nazwe")
                .setView(input)
                .setPositiveButton("Zapisz", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        helper.saveNewCommand(lastPosition, input.getText().toString());
                        invalidateListView();
                    }
                });
        builder.show();
    }
}
