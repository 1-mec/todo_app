package com.todoapp.fr;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.todoapp.fr.sql.NameDataSource;
import com.todoapp.fr.sql.Tasks;
import com.todoapp.fr.sql.DataBaseFunctions;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final TaskRelated tr = new TaskRelated();
    private ArrayList<CheckBox> lstChbx ;
    private Button ajouter;
    private LinearLayout layoutVertical;
    private ScrollView mainFrame;
    private Button supprimer;
    private Button verif;
    private ArrayList<String> tmp ;
    private String inputDialog ;
    private NameDataSource datasource;
    private DataBaseFunctions db ;
    private ArrayList<Tasks> allTasks ;
    private ArrayList<Tasks> selectedTask;
    private FrameLayout btn_container;
    private ScrollView scroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);


        mainFrame = findViewById(R.id.mainFrame);
        ajouter = findViewById(R.id.ajouter);
        supprimer = findViewById(R.id.supprimer);
        verif = findViewById(R.id.verif);
        btn_container = findViewById(R.id.btn_container);

        datasource = new NameDataSource(this);
        datasource.open();
        db = new DataBaseFunctions(this);
        allTasks = datasource.getAllTasks();
        scroll = new ScrollView(this);
        scroll.canScrollVertically(1);
        layoutVertical = new LinearLayout(this);
        layoutVertical.setOrientation(LinearLayout.VERTICAL);
        inputDialog = "";
        tmp = tr.getTasks();
        lstChbx = new ArrayList<>();
        selectedTask = new ArrayList<>();
        btn_container.post(() -> {
            int height = btn_container.getHeight();
            mainFrame.setTranslationY(height);
        });

        display();

        ajouter.setOnClickListener(v -> {
            AlertDialog.Builder alertAdd = new AlertDialog.Builder(this);
            alertAdd.setTitle("Ajout de la tâche");
            alertAdd.setIcon(R.drawable.ic_launcher_foreground);
            final EditText input = new EditText(MainActivity.this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            alertAdd.setView(input);
            alertAdd.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    inputDialog = input.getText().toString();
                    System.out.println(inputDialog);
                    db.addTaskDb(inputDialog);
                    System.out.println("------" + allTasks + "-----------");
                    refresh();
                }
            });
            alertAdd.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = alertAdd.create();
            alertDialog.show();
            display();
        });

        verif.setOnClickListener(v -> {
            test();
        });

        supprimer.setOnClickListener(v -> {
            AlertDialog.Builder alertDelete = new AlertDialog.Builder(this);
            try {
                alertDelete.setTitle("Suppression de tâchê(s)");
                alertDelete.setIcon(R.drawable.ic_launcher_foreground);
                alertDelete.setPositiveButton("toutes les tâches", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        datasource.deleteAllTasks();
                        refresh();
                    }
                });
                alertDelete.setNegativeButton("tâches séléctionnées", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        deleteSelectedOne();
                        refresh();
                    }
                });
                alertDelete.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
            } catch (Exception e) {
                alertDelete.setTitle("Désolé , ça n'a pas marché");
                alertDelete.setIcon(R.drawable.ic_launcher_foreground);
                alertDelete.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        refresh();
                    }
                });
            }
            AlertDialog alertDialog = alertDelete.create();
            alertDialog.show();
            display();
        });

        for (CheckBox ch : lstChbx){
            ch.setOnClickListener(v -> {
                addSelectTask();
            });
        }


    }

    public void display(){
        if (mainFrame == null) return;
        mainFrame.removeAllViews();
        if (allTasks == null || datasource.isTasksEmpty()){
            TextView tv = new TextView(this);
            tv.setText(R.string.lorem_ipsum);
            tv.setGravity(Gravity.CENTER);
            tv.setTextSize(36);
            tv.setTextColor(Color.parseColor("#666666"));
            mainFrame.addView(tv);
        } else {
            layoutVertical.removeAllViews();
            for (Tasks tasks : allTasks){
                layoutVertical.addView(makeCheckBox(tasks.toString()));
            }
            if (layoutVertical.getParent() != null) {
                ((FrameLayout)layoutVertical.getParent()).removeView(layoutVertical);
            }
            mainFrame.addView(layoutVertical);
        }
    }

    public CheckBox makeCheckBox(String act){
        CheckBox ch = new CheckBox(this);
        ch.setChecked(false);
        ch.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        ch.setText(act);
        ch.setTextColor(Color.parseColor("black"));
        ch.setTextSize(24);
        lstChbx.add(ch);
        return ch ;
    }

    public void test(){
        AlertDialog.Builder alertVerif;
        System.out.println("DataBaseFunctions => " + lstChbx.toString());
        if (!isAllChecked()){
            alertVerif = new AlertDialog.Builder(this);
            alertVerif.setTitle("Désolé , il reste des articles");
            alertVerif.setIcon(R.drawable.ic_launcher_foreground);
            alertVerif.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {}
            });
        } else {
            alertVerif = new AlertDialog.Builder(this);
            alertVerif.setTitle("Vous avez tous prit !");
            alertVerif.setIcon(R.drawable.ic_launcher_foreground);
            alertVerif.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {}
            });
        }
        AlertDialog alertDialog = alertVerif.create();
        alertDialog.show();

    }

    public boolean isAllChecked(){
        boolean res = true;
        for (CheckBox ch : lstChbx){
            if (!ch.isChecked()){
                res = false;
            }
        }
        return res;
    }

    public void deleteSelectedOne(){
        for (Tasks ch : selectedTask){
            datasource.deleteTasks(ch);
        }
    }

    public void refresh(){
        finish();
        startActivity(getIntent());
    }

    public void addSelectTask(){
        for(int i = 0 ; lstChbx.size() > i ; i++){
            if(lstChbx.get(i).isChecked()){
                selectedTask.add(allTasks.get(i));
            }
        }
    }
}
