package de.thm.ap.ap_przb86_u1;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import de.thm.ap.ap_przb86_u1.model.Record;

class RecordDAO {
    private static String FILE_NAME = "records.obj";

    private Context ctx;
    private List<Record> records;
    private int nextId = 1;

    public RecordDAO(Context ctx){
        this.ctx = ctx;
        initRecords();
    }

    public void close(){
        File f = ctx.getFileStreamPath(FILE_NAME);

        if(f.exists()){
            f.delete();
        }
    }

    public List<Record> findAll(){
        return records;
    }

    public Integer persist(Record record){
        record.setId(nextId);
        records.add(record);
        saveRecords();

        return nextId++;
    }

    public boolean update(Record record){
//        records = records.stream().map(o -> {
//            o.getId() != record.getId() ? o : record}).collect(Collectors.toList());

        boolean saved = false;

        for(int i = 0; i < records.size(); i++){
            if(record.getId() == records.get(i).getId()){
                records.set(i, record);

                saved = records.get(i).equals(record);
            }
        }

        saveRecords();

        return saved;
    }

    public Optional<Record> findById(int id){
        Optional<Record> optionalRecord = Optional.ofNullable(null);

        for(int i = 0; i < records.size(); i++){
            if(records.get(i).getId() == id)
                optionalRecord = Optional.of(records.get(i));
        }

        return optionalRecord;
//        return records.stream().filter(record -> id == record.getId()).findFirst();
    }

    @SuppressWarnings("unchecked")
    public void initRecords(){
        File f = ctx.getFileStreamPath(FILE_NAME);

        if(f.exists()){
            try(FileInputStream in = ctx.openFileInput(FILE_NAME)){
                Object obj = new ObjectInputStream(in).readObject();
                records = (List<Record>) obj;
                records.stream().mapToInt(Record::getId).max().ifPresent(id -> nextId = id + 1);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
               records = new ArrayList<>();
        }
    }

    private void saveRecords(){
        try(FileOutputStream out = ctx.openFileOutput(FILE_NAME, Context.MODE_PRIVATE)){
            new ObjectOutputStream(out).writeObject(records);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}