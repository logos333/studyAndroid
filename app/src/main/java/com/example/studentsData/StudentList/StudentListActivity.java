package com.example.studentsData.StudentList;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.studentsData.MyToast;
import com.example.studentsData.R;

import java.util.ArrayList;

public class StudentListActivity extends AppCompatActivity {
    public static final String STUDENTS_DATA_FILE = "students.dat";

    ArrayList<Student> students = new ArrayList<>();
    ArrayList<Student> _students = new ArrayList<>();
    StudentAdapter listViewAdapter, _listViewAdapter, gridViewAdapter, _gridViewAdapter;
    TextView searchTxt;
    ListView lv;
    GridView gv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_students);

        gv = findViewById(R.id.gridView);
        lv = findViewById(R.id.list);
        lv.setOnItemClickListener(onListItemClick);
        lv.setLongClickable(true);
        lv.setOnItemLongClickListener(onItemLongClickListener);
        searchTxt = findViewById(R.id.searchTxt);
        searchTxt.addTextChangedListener(textWatcher);
//        writeData(new Student("16210525", "Tim", "male", 21));

        //get student from data source(file)
        students = DataControl.getStudentsFromFile(this);

        lv.setAdapter(listViewAdapter = new StudentAdapter(this, R.layout.student_list_element, students));
        _listViewAdapter = new StudentAdapter(this, R.layout.student_list_element, _students);
// students
        gv.setAdapter(gridViewAdapter = new StudentAdapter(this, R.layout.grid_view_item, students));
        _gridViewAdapter = new StudentAdapter(this, R.layout.grid_view_item, _students);

        switch_view();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //saving the changed mode of visibility of listview
        getSharedPreferences("ViewPreferences", MODE_PRIVATE).edit().putBoolean("ListView_Visibility", lv.getVisibility()==View.VISIBLE).apply();

        //item is only one (switch view), so i will just make one method for it
        switch_view();
        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        menu.add(1, 1, 0, "First");
//    }

    /**
     * switching beetween listview and gridview according to sharedpreferences in the "ViewPreferences" file
     */
    private void switch_view() {
        SharedPreferences sp = getSharedPreferences("ViewPreferences", MODE_PRIVATE);

        //if listview is visible
        if(sp.getBoolean("ListView_Visibility", true)) {
            lv.setVisibility(View.GONE);
            gv.setVisibility(View.VISIBLE);
        } else {
            gv.setVisibility(View.GONE);
            lv.setVisibility(View.VISIBLE);
        }
    }


    private void initialData() {
        students = DataControl.getStudentsFromFile(this);
        apply();
    }

    public void btnAdd_Click(View view) {
        Intent intent = new Intent(this, AddStudentActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    void apply() {
        if(lv.getVisibility() == View.VISIBLE)
            ((StudentAdapter) lv.getAdapter()).notifyDataSetChanged();
        else
            ((StudentAdapter) gv.getAdapter()).notifyDataSetChanged();
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
                    if (student.getSid().toLowerCase().contains(text) || student.getSname().toLowerCase().contains(text)) {
                        _students.add(student);
                    }
                }

               if(lv.getVisibility() == View.VISIBLE)
                   lv.setAdapter(_listViewAdapter);
               else
                   gv.setAdapter(_gridViewAdapter);

            } else {

                if(lv.getVisibility() == View.VISIBLE)
                    lv.setAdapter(listViewAdapter);
                else
                    gv.setAdapter(gridViewAdapter);
            }
            apply();
        }
    };


    AdapterView.OnItemClickListener onListItemClick = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent = new Intent(getApplicationContext(), AddStudentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("student", students.get(position));
            startActivity(intent);
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

