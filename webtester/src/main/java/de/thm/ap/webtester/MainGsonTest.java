package de.thm.ap.webtester;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MainGsonTest {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void main(String[] args) {
        List<TaskGson> list = new ArrayList<>();
        for(int i = 0; i < 20; i++){
            list.add(new TaskGson(i, "Test1", "Test2",
                         TaskGson.Status.COMPLETED, 10));
        }

        Gson gson = new Gson();
        Type type = new TypeToken<List<TaskGson>>(){}.getType();
        String json = gson.toJson(list, type);
        System.out.println(json);
        List<TaskGson> fromJson = gson.fromJson(json, type);

        fromJson.forEach(System.out::println);
    }
}
