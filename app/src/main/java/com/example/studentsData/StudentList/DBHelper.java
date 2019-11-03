package com.example.studentsData.StudentList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.studentsData.MyToast;
import com.example.studentsData.R;

import java.io.ObjectOutputStream;
import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "StudentsDB";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_STUDENTS = "students";

    public static final String KEY_ID = "_id";
    public static final String KEY_STUDENT_ID = "sid";
    public static final String KEY_NAME = "name";
    public static final String KEY_SEX = "sex";
    public static final String KEY_BIRTHDAY = "birthday";
    public static final String KEY_LANGUAGES = "languages";
    public static final String KEY_IMAGE = "image";

    private static ArrayList<Student> students = new ArrayList<>();
    private static Context context;



    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_STUDENTS + " ("
                + KEY_ID + " integer primary key, "
                + KEY_STUDENT_ID + " text, "
                + KEY_NAME + " text, "
                + KEY_SEX + " text, "
                + KEY_BIRTHDAY + " text, "
                + KEY_LANGUAGES + " text, "
                + KEY_IMAGE + " text"
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_STUDENTS);

        onCreate(db);
    }

    public static ArrayList<Student> getStudentList(StudentListActivity _context) {
        students.clear();
        context = _context;
        try(SQLiteDatabase sd = new DBHelper(context).getReadableDatabase()) {
            Cursor cursor = sd.query(TABLE_STUDENTS, null, null, null, null, null, null);


            if(cursor.moveToFirst()) {

                int id = cursor.getColumnIndex(KEY_STUDENT_ID);
                int name = cursor.getColumnIndex(KEY_NAME);
                int sex = cursor.getColumnIndex(KEY_SEX);
                int birthday = cursor.getColumnIndex(KEY_BIRTHDAY);
                int languages = cursor.getColumnIndex(KEY_LANGUAGES);
                int image = cursor.getColumnIndex(KEY_IMAGE);
                do {
                    students.add(new Student(cursor.getString(id),
                            cursor.getString(name),
                            cursor.getString(sex),
                            cursor.getString(birthday),
                            cursor.getInt(languages),
                            cursor.getString(image)));
                }while (cursor.moveToNext());

            }else {
                throw new Exception("cannot read the database!");
            }
        }catch (Exception ex){
            MyToast.showMessage(_context, ex.getMessage());
            Log.d("MyLogs", ex.getMessage());
        }

        return students;
    }

    public static void addStudent(Student student) throws Exception {
        for (Student s : students) {
            if (s.getSid().equals(student.getSid()))
                throw new Exception(context.getString(R.string.sid));
        }

        writeData(student);
        students.add(student);
    }

    private static void writeData(Student student) throws Exception {
        try(SQLiteDatabase sd = new DBHelper(context).getWritableDatabase()) {
            sd.insert(TABLE_STUDENTS, null, student.getContentValues());
        }
    }

    public static boolean deleteStudent(String sid) throws Exception{
        for (Student s : students) {
            if (s.getSid().equals(sid)) {

                try(SQLiteDatabase sd = new DBHelper(context).getWritableDatabase()) {
                    sd.delete(TABLE_STUDENTS, KEY_STUDENT_ID + " = ?", new String[] {sid});
                    students.remove(s);
                }
                return true;
            }
        }
        return false;
    }
}
