package com.example.todolist.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.todolist.TaskItem;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "toDoListDatabase";
    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String DESC = "desc";
    private static final String STATUS = "status";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + " ("
            + ID + " TEXT PRIMARY KEY, "
            + TASK + " TEXT, "
            + DESC + " TEXT, "
            + STATUS + " INTEGER)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }



    public LiveData<List<TaskItem>> getAllTasksLive() {
        MutableLiveData<List<TaskItem>> liveData = new MutableLiveData<>();
        List<TaskItem> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try {
            cur = db.query(TODO_TABLE, null, null, null, null, null, null);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        int taskIndex = cur.getColumnIndex(TASK);
                        int descIndex = cur.getColumnIndex(DESC);
                        int statusIndex = cur.getColumnIndex(STATUS);
                        int idIndex = cur.getColumnIndex(ID);

                        // Check for invalid column indexes
                        if (taskIndex == -1 || descIndex == -1 || statusIndex == -1 || idIndex == -1) {
                            throw new IllegalArgumentException("Invalid column index");
                        }

                        TaskItem task = new TaskItem(
                                cur.getString(taskIndex),
                                cur.getString(descIndex),
                                cur.getInt(statusIndex) == 1 ? LocalDate.now() : null,
                                UUID.fromString(cur.getString(idIndex))
                        );
                        taskList.add(task);
                    } while (cur.moveToNext());
                }
            }
        } finally {
            db.endTransaction();
            if (cur != null) {
                cur.close();
            }
        }
        liveData.setValue(taskList);
        return liveData;
    }

    public void updateTask(UUID id, String task, String desc) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        cv.put(DESC, desc);
        db.update(TODO_TABLE, cv, ID + "=?", new String[]{id.toString()});
    }
}
