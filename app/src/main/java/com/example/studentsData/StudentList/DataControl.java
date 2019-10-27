package com.example.studentsData.StudentList;

import android.content.Context;

import com.example.studentsData.MyToast;
import com.example.studentsData.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

public abstract class DataControl {

    private static ArrayList<Student> students = new ArrayList<>();
    private static StudentListActivity context = null;

    public static ArrayList<Student> getStudentsFromFile(StudentListActivity context) {
        DataControl.context = context;
        try {
            File file = new File(context.getFilesDir(), "/" + context.STUDENTS_DATA_FILE);
            if (!file.exists()) {
                file.createNewFile();
            }


            try (FileInputStream fis = new FileInputStream(file)) {
                try (ObjectInputStream ois = new ObjectInputStream(fis)) {
                    students = (ArrayList<Student>) ois.readObject();
                }
            }

        } catch (IOException i) {

        } catch (Exception ex) {
            MyToast.showMessage(context, ex.getMessage());
        }
        return students;
    }

    public static ArrayList<Student> getStudents() {
        return students;
    }


    public static void addStudent(Student student) throws Exception {
        for (Student s : students) {
            if (s.getSid().equals(student.getSid()))
                throw new Exception(context.getString(R.string.sid));
        }

        writeData(student);
    }

    private static void writeData(Student student) throws Exception {
        try (ObjectOutputStream oos = new ObjectOutputStream(context.openFileOutput(context.STUDENTS_DATA_FILE, Context.MODE_PRIVATE))) {
            if (student != null)
                students.add(student);
            oos.writeObject(students);
        }
    }

    public static boolean deleteStudent(String sid) throws Exception{
        for (Student s : students) {
            if (s.getSid().equals(sid)) {
                students.remove(s);
                writeData(null);
                return true;
            }
        }
        return false;
    }


}
