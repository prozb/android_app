package de.thm.ap.webtester;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainGsonTest {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String[] args) throws IOException {

//        File file = new File("modules.json");
//        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
//        writer.write("hello pidors");
//        writer.flush();
//        writer.close();

//        InputStreamReader reader = new InputStreamReader(new FileInputStream("modules.json"));
//        BufferedReader reader = new BufferedReader(new FileReader("modules.json"));
//
//        String line = reader.readLine();
//        while (line != null){
//            System.out.println(line);
//            line = reader.readLine();
//        }
        Gson gson = new Gson();
        Module [] modules = gson.fromJson(new FileReader("modules.json"), Module[].class);

        for(int i = 0; i < modules.length; i++){
            System.out.println(modules[i].getName() + " " + modules[i].getNr() + " " + modules[i].getCrp());
        }

//        System.out.println(file.getAbsolutePath());
//        System.out.println(file.getCanonicalPath());
//        List<TaskGson> list = new ArrayList<>();
//        for(int i = 0; i < 20; i++){
//            list.add(new TaskGson(i, "Test1", "Test2",
//                         TaskGson.Status.COMPLETED, 10));
//        }
//
//        Gson gson = new Gson();
//        Type type = new TypeToken<List<TaskGson>>(){}.getType();
//        String json = gson.toJson(list, type);
//        System.out.println(json);
//        List<TaskGson> fromJson = gson.fromJson(json, type);
//
//        fromJson.forEach(System.out::println);
    }
}
