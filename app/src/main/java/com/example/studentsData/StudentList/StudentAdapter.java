package com.example.studentsData.StudentList;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.studentsData.R;

import java.util.List;

public class StudentAdapter extends ArrayAdapter<Student> {

    private LayoutInflater inflater;
    private int layout;
    private List<Student> students;


    public StudentAdapter(@NonNull Context context, int resource, List<Student> students) {
        super(context, resource, students);
        this.students = students;
        layout = resource;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(this.layout, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Student student = students.get(position);

        //setting the student's info
        viewHolder.sidView.setText(student.getSid());
        viewHolder.snameView.setText(student.getSname());
        viewHolder.imageView.setImageURI(Uri.parse(student.getPhoto()));

        return convertView;
    }


    //inner private class for optimize findViewById() method
    private class ViewHolder {
        final TextView snameView, sidView;
        final ImageView imageView;

        ViewHolder(View view) {
            sidView = view.findViewById(R.id.sid);
            snameView =  view.findViewById(R.id.sname);
            imageView = view.findViewById(R.id.imageView);
        }
    }

}
