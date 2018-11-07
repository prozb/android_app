package de.thm.ap.ap_przb86_u1;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;

import java.io.FileNotFoundException;

public class AppContentProvider extends ContentProvider {
    public static String AUTHORITY = "de.thm.ap.ap_przb86_u1";

    private static String RECORD_PATH = "record";
    private static UriMatcher URI_MATCHER;
    private static final int RECORDS   = 1;
    private static final int RECORD_ID = 2;

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
                return "vnd.android.cursor.dir/vnd.thm.ap.record";
            case RECORD_ID:
                return "vnd.android.cursor.item/vnd.thm.ap.record";
            default:
                    throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Override
    public ParcelFileDescriptor openFile(Uri uri,String mode, CancellationSignal signal) throws FileNotFoundException {
        return super.openFile(uri, mode, signal);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
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
}
