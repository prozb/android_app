package de.thm.ap.ap_przb86_u1.cp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.db.SupportSQLiteQueryBuilder;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.util.List;
import java.util.Objects;

import de.thm.ap.ap_przb86_u1.AppDatabase;
import de.thm.ap.ap_przb86_u1.RecordsActivity;
import de.thm.ap.ap_przb86_u1.model.Record;

public class AppContentProvider extends ContentProvider {
    public static String AUTHORITY = "de.thm.ap.ap_przb86_u1.cp";

    private static String RECORD_PATH = "records_menu-db";
    private static UriMatcher URI_MATCHER;
    private static final int RECORDS   = 1;
    private static final int RECORD_ID = 2;
    private static final int LOL_TXT   = 3;
    private static StringBuilder sb = new StringBuilder();

    static {
        URI_MATCHER = new UriMatcher(URI_MATCHER.NO_MATCH);
        // content://de.thm.ap_przb86_u1/record
        URI_MATCHER.addURI(AUTHORITY, RECORD_PATH, RECORDS);
        // content://de.thm.ap_przb86_u1/record/# (#placeholder)
        URI_MATCHER.addURI(AUTHORITY, RECORD_PATH + "/#", RECORD_ID);
    }
    // cr.openFileDescriptor
    // cr.getFileDescriptor
    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public String getType(Uri uri) {
        switch (URI_MATCHER.match(uri)){
            case RECORDS:
                return "vnd.android.cursor.dir/vnd.thm.ap.ap_przb86_u1";
            case RECORD_ID:
                return "vnd.android.cursor.item/vnd.thm.ap.ap_przb86_u1";
            default:
                    throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SupportSQLiteQueryBuilder builder = SupportSQLiteQueryBuilder
                .builder("Record")
                .columns(projection)
                .orderBy(sortOrder);
        Log.e("QUERRY", "LOL?s" + URI_MATCHER.match(uri));
        switch (URI_MATCHER.match(uri)){
            case RECORDS:
                break;
            case RECORD_ID:
                builder.selection("id = ?", new Object [] {uri.getLastPathSegment()});
                break;

                default:
                    throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        SupportSQLiteDatabase db = AppDatabase.getDb(getContext())
                                              .getOpenHelper()
                                              .getReadableDatabase();
        return db.query(builder.create());
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        File file = new File(getContext().getFilesDir(), "lol.csv");
        ParcelFileDescriptor pfd;
        Log.d("CREATING", "created file");

        String toWrite = createCSVData();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(writer != null){
            try {
                writer.write(toWrite);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d("CREATED", "file sent");
        pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        return pfd;
    }

    private String getStringifiedRecords(){
        return "Hello world";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    private String createCSVData(){
        sb.setLength(0);
        SupportSQLiteQueryBuilder builder = SupportSQLiteQueryBuilder.builder("Record");
        SupportSQLiteDatabase db = AppDatabase.getDb(getContext()).getOpenHelper().getReadableDatabase();
        Cursor c = db.query(builder.create());

        if(c != null){
            c.moveToFirst();
            for(int i = 0; i < c.getCount(); i++){
                for(int j = 0; j < c.getColumnNames().length; j++){
                    sb.append(c.getString(j));
                    sb.append(",");
                }
                sb.append("\n");
                c.moveToNext();
            }
            c.close();
        }

        return sb.toString();
    }
}
