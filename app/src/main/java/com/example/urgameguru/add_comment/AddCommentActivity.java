package com.example.urgameguru.add_comment;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.urgameguru.R;
import com.example.urgameguru.bean.Comment;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.perf.FirebasePerformance;
import com.google.firebase.perf.metrics.Trace;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddCommentActivity extends Activity {

    private static final String TAG = "addComment";

    FirebaseUser user;
    DatabaseReference databaseRef;

    TextView tvGameName;
    EditText etCommentContent, etRate;
    Button btSubmit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_comment);

        tvGameName = findViewById(R.id.tv_add_comment_game_name);
        etCommentContent = findViewById(R.id.et_comment_content);
        etRate = findViewById(R.id.et_comment_rate);
        btSubmit = findViewById(R.id.bt_submit_comment);

        String gameName = getIntent().getStringExtra("name");
        tvGameName.setText(gameName);

        btSubmit.setOnClickListener(v -> {
            Trace myTrace = FirebasePerformance.getInstance().newTrace("AddComment_trace");
            myTrace.start();

            databaseRef = FirebaseDatabase.getInstance("https://urgameguru-it5007-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
            user = FirebaseAuth.getInstance().getCurrentUser();

            Date currentDate = Calendar.getInstance().getTime();
            System.out.println("Current time => " + currentDate);

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String formattedDate = df.format(currentDate);

            Comment comment = new Comment(user.getEmail(), etCommentContent.getText().toString(), formattedDate, etRate.getText().toString());
            DatabaseReference commentRef = databaseRef.child("comment").child(gameName).child(user.getUid());

            commentRef.setValue(comment).addOnSuccessListener(taskSnapshot -> {
                Toast.makeText(this, "Submit comment success!", Toast.LENGTH_SHORT).show();
                myTrace.stop();
                finish();
            });

        });
    }
}
