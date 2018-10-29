package de.thm.ap.ap_przb86_u1;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import de.thm.ap.ap_przb86_u1.model.Record;

public class RecordsActivity extends AppCompatActivity {
    private static boolean DEBUGGING = true;
    private boolean INITIALIZED = false;
    private ListView recordListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new RecordDAO(this).close();

        setContentView(R.layout.activity_records);

        recordListView = findViewById(R.id.records_list);
        recordListView.setEmptyView(findViewById(R.id.records_list_empty));
        recordListView.setOnItemClickListener((parent, view, position, id) -> {
            Intent i = new Intent(RecordsActivity.this, RecordFormActivity.class);
            i.putExtra("position", position);

            startActivity(i);
        });
        recordListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
//        recordListView.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                return false;
//            }
//        });
        recordListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                Toast.makeText(RecordsActivity.this, "chosed", Toast.LENGTH_SHORT).show();
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) { // Inflate the menu for the CAB
                getMenuInflater().inflate(R.menu.choose_menu, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_delete:
                        Log.d("PRESSED", "delete action in choose menu performed");
                        // TODO: Perform delete action
                        deleteSelectedItems();
                        mode.finish();
                        return true;
                    case R.id.action_mail:
                        Log.d("PRESSED", "mail action in choose menu performed");
                        // TODO: perform mail action
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
//                mode = null;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.records, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d("ADD", "onOptionsItemSelected");

        switch(item.getItemId()) {
            case R.id.action_add:
                Log.d("PRESSED", "add module action in menu performed");
                Intent i = new Intent(this, RecordFormActivity.class);
                startActivity(i);
                return true;

            case R.id.action_stats:
                Log.d("PRESSED", "statistics action in menu performed");
                showStatistic();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteSelectedItems(){
//        int selected = recordListView.getCheckedItemCount();
//        Toast.makeText(this, "removing " + selected + " items", Toast.LENGTH_SHORT).show();

        new AlertDialog.Builder(this).
                setTitle(R.string.confirmation).
                setMessage(R.string.confirmation_message).
                setIcon(R.drawable.id_dialog_alert_24dp).
                setPositiveButton(R.string.yes, (dialog, which) -> {

//                    recordListView.cl
//                    Log.d?("ALENGTH", "" + x.length);


                    Log.d("PRESSED", "confirmed removing modules");
                }).
                setNegativeButton(R.string.no, (dialog, which) -> Log.d("PRESSED", "don't remove items")).
                show();
    }
    private void showStatistic() {
        Stats stats = new Stats(new RecordDAO(this).findAll());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.stats);
        builder.setMessage("Leistungen: " + stats.getSumModules()
                + "\n50% Leistungen: " + stats.getSumHalfWeighted()
                + "\nSumme Crp: " + stats.getSumCrp()
                + "\nDurchschnitt: " + stats.getAverageMark()
                + "%\nCrp bis Ziel: " + stats.getCrpToEnd());
        builder.setNeutralButton(R.string.close, null);
        builder.show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(DEBUGGING && !INITIALIZED){
            RecordDAO recordDAO = new RecordDAO(this);
            recordDAO.persist(new Record("CS1013", "Objektorientierte Programmierung", 2016, true, true, 6, 73));
            recordDAO.persist(new Record("MN1007", "Diskrete Mathematik", 2016, false, true, 6, 81));
            recordDAO.persist(new Record("CS1019", "Compilerbau", 2017, false, false, 6, 81));
            recordDAO.persist(new Record("CS1020", "Datenbanksysteme", 2017, false, false, 6, 92));

            INITIALIZED = true;
        }

        List<Record> records = new RecordDAO(this).findAll();

        ArrayAdapter<Record> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_activated_1, records);

        recordListView.setAdapter(adapter);
     }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        new RecordDAO(this).close();
    }
}
