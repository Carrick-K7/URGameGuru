package com.example.urgameguru.add_article;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import jp.wasabeef.richeditor.RichEditor;

import com.example.urgameguru.R;
import com.example.urgameguru.bean.Article;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ArticlePageActivity extends Activity {

    private static final String TAG = "articlePage";

    FirebaseUser user;
    StorageReference storageRef;
    DatabaseReference databaseRef;

    EditText etArticleName;
    RichEditor mEditor;
    TextView mPreview;
    Button btSave, btEdit;
    ProgressBar pbSave;

    String articleType, articleStr;
    String gameName;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_page);

        etArticleName = findViewById(R.id.et_article_name);
        mEditor = findViewById(R.id.editor);
        mPreview = findViewById(R.id.tv_preview);
        btEdit = findViewById(R.id.bt_edit);
        btSave = findViewById(R.id.bt_save);
        pbSave = findViewById(R.id.pb_article_save);

        setUpEditor();

        Intent intent = getIntent();
        String uri = intent.getStringExtra("articleUri");
        String name = intent.getStringExtra("articleName");
        if (uri != null) {
            ExecutorService executor = Executors.newSingleThreadExecutor();
            Handler handler = new Handler(Looper.getMainLooper());

            executor.execute(() -> {
                try {
                    URL url = new URL(uri);
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String str;
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((str = in.readLine()) != null) {
                        stringBuilder.append(str);
                    }
                    articleStr = stringBuilder.toString();
                    Log.d(TAG, articleStr);
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                handler.post(() -> {
                    etArticleName.setText(name);
                    mEditor.setHtml(articleStr);
                    mEditor.setInputEnabled(false);
                    btEdit.setVisibility(View.VISIBLE);
                });
            });

        }

        user = FirebaseAuth.getInstance().getCurrentUser();
        storageRef = FirebaseStorage.getInstance().getReference();
        databaseRef = FirebaseDatabase.getInstance("https://urgameguru-it5007-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();
        gameName = "Wild Rift";
        articleType = "review";

        btSave.setOnClickListener(v -> {
            pbSave.setVisibility(View.VISIBLE);
            btEdit.setVisibility(View.VISIBLE);
            mEditor.setInputEnabled(false);

            // TODO Delete old file if user change the article name
            String storageUploadPath = "articles/" + gameName + "/" + articleType + "/" + user.getUid() + "/" + etArticleName.getText().toString();
            Log.d(TAG, storageUploadPath);
            StorageReference uploadRef = storageRef.child(storageUploadPath);
            InputStream stream = new ByteArrayInputStream(mEditor.getHtml().getBytes(StandardCharsets.UTF_8));
            UploadTask uploadTask = uploadRef.putStream(stream);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                uploadRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                    Article article = new Article(etArticleName.getText().toString(), downloadUri.toString());
                    String dbUploadPath = "articles/" + gameName + "/" + articleType + "/" + user.getUid() + "/" + etArticleName.getText().toString();
                    databaseRef.child(dbUploadPath).setValue(article);
                    pbSave.setVisibility(View.INVISIBLE);
                });
            }).addOnFailureListener(e -> {
                Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
            });
        });

        btEdit.setOnClickListener(v -> {
            btEdit.setVisibility(View.INVISIBLE);
            mEditor.setInputEnabled(true);
        });

    }

    public void setUpEditor() {
        mEditor.setEditorFontSize(22);
        mEditor.setEditorFontColor(Color.BLACK);
        mEditor.setPadding(10, 10, 10, 10);
        mEditor.setPlaceholder("Insert text here...");

        mEditor.setOnTextChangeListener(text -> mPreview.setText(text));

        findViewById(R.id.action_undo).setOnClickListener(v -> mEditor.undo());
        findViewById(R.id.action_redo).setOnClickListener(v -> mEditor.redo());
        findViewById(R.id.action_bold).setOnClickListener(v -> mEditor.setBold());
        findViewById(R.id.action_italic).setOnClickListener(v -> mEditor.setItalic());
        findViewById(R.id.action_subscript).setOnClickListener(v -> mEditor.setSubscript());
        findViewById(R.id.action_superscript).setOnClickListener(v -> mEditor.setSuperscript());
        findViewById(R.id.action_strikethrough).setOnClickListener(v -> mEditor.setStrikeThrough());
        findViewById(R.id.action_underline).setOnClickListener(v -> mEditor.setUnderline());
        findViewById(R.id.action_heading1).setOnClickListener(v -> mEditor.setHeading(1));
        findViewById(R.id.action_heading2).setOnClickListener(v -> mEditor.setHeading(2));
        findViewById(R.id.action_heading3).setOnClickListener(v -> mEditor.setHeading(3));
        findViewById(R.id.action_heading4).setOnClickListener(v -> mEditor.setHeading(4));
        findViewById(R.id.action_heading5).setOnClickListener(v -> mEditor.setHeading(5));
        findViewById(R.id.action_heading6).setOnClickListener(v -> mEditor.setHeading(6));

        findViewById(R.id.action_txt_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextColor(isChanged ? Color.BLACK : Color.RED);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_bg_color).setOnClickListener(new View.OnClickListener() {
            private boolean isChanged;

            @Override
            public void onClick(View v) {
                mEditor.setTextBackgroundColor(isChanged ? Color.TRANSPARENT : Color.GREEN);
                isChanged = !isChanged;
            }
        });

        findViewById(R.id.action_indent).setOnClickListener(v -> mEditor.setIndent());
        findViewById(R.id.action_outdent).setOnClickListener(v -> mEditor.setOutdent());
        findViewById(R.id.action_align_left).setOnClickListener(v -> mEditor.setAlignLeft());
        findViewById(R.id.action_align_center).setOnClickListener(v -> mEditor.setAlignCenter());
        findViewById(R.id.action_align_right).setOnClickListener(v -> mEditor.setAlignRight());
        findViewById(R.id.action_blockquote).setOnClickListener(v -> mEditor.setBlockquote());
        findViewById(R.id.action_insert_bullets).setOnClickListener(v -> mEditor.setBullets());
        findViewById(R.id.action_insert_numbers).setOnClickListener(v -> mEditor.setNumbers());
        findViewById(R.id.action_insert_image).setOnClickListener(v -> mEditor.insertImage("https://raw.githubusercontent.com/wasabeef/art/master/chip.jpg",
                "dachshund", 320));
        findViewById(R.id.action_insert_youtube).setOnClickListener(v -> mEditor.insertYoutubeVideo("https://www.youtube.com/embed/pS5peqApgUA"));
        findViewById(R.id.action_insert_audio).setOnClickListener(v -> mEditor.insertAudio("https://file-examples-com.github.io/uploads/2017/11/file_example_MP3_5MG.mp3"));
        findViewById(R.id.action_insert_video).setOnClickListener(v -> mEditor.insertVideo("https://test-videos.co.uk/vids/bigbuckbunny/mp4/h264/1080/Big_Buck_Bunny_1080_10s_10MB.mp4", 360));
        findViewById(R.id.action_insert_link).setOnClickListener(v -> mEditor.insertLink("https://github.com/wasabeef", "wasabeef"));
        findViewById(R.id.action_insert_checkbox).setOnClickListener(v -> mEditor.insertTodo());

    }

}