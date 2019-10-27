package com.example.studentsData.StudentList;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.studentsData.MyToast;
import com.example.studentsData.R;

import java.util.ArrayList;

public class StudentListActivity extends ListActivity {
    public static final String STUDENTS_DATA_FILE = "students.dat";

    ArrayList<Student> students = new ArrayList<>();
    ArrayList<Student> _students = new ArrayList<>();
    StudentAdapter adapter, _adapter;
    TextView searchTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_students);

        getListView().setLongClickable(true);
        getListView().setOnItemLongClickListener(onItemLongClickListener);
        searchTxt = findViewById(R.id.searchTxt);
        searchTxt.addTextChangedListener(textWatcher);
//        writeData(new Student("16210525", "Tim", "male", 21));
        students = DataControl.getStudentsFromFile(this);
        setListAdapter(adapter = new StudentAdapter(this, R.xml.student_list_element, students));
        _adapter = new StudentAdapter(this, R.xml.student_list_element, _students);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialData();
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.add(1, 1, 0, "First");
//    }


    private void initialData() {
        students = DataControl.getStudentsFromFile(this);
        apply();
    }

    public void btnAdd_Click(View view) {
        Intent intent = new Intent(this, AddStudentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent intent = new Intent(getApplicationContext(), AddStudentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("student", students.get(position));
        startActivity(intent);
    }


    void apply() {
        ((StudentAdapter) getListAdapter()).notifyDataSetChanged();
    }

    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            _students.clear();
            String text = s.toString();
            if (!s.toString().equals("")) {
                for (Student student : students) {
                    if (student.getSid().contains(text) || student.getSname().contains(text)) {
                        _students.add(student);
                    }
                }

                setListAdapter(_adapter);
            } else {

                setListAdapter(adapter);
            }
            apply();
        }
    };

   AdapterView.OnItemLongClickListener onItemLongClickListener = new AdapterView.OnItemLongClickListener() {
       @Override
       public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
           MyToast.showMessage(getApplicationContext(), "longClick");
           return true;
       }
   };
}

