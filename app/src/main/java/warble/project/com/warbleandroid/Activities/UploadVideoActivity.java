package warble.project.com.warbleandroid.Activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import warble.project.com.warbleandroid.R;
import warble.project.com.warbleandroid.Utils.VideoData;

public class UploadVideoActivity extends AppCompatActivity {

    private static final int PICK_VIDEO_REQUEST = 8;
    VideoView videoView;
    ImageView imageView;
    EditText editText;
    Button button;

    static Uri videoUri;
    Uri imageUri;
    private StorageReference mStorageRef;
    private UploadTask mUploadTask;
    private String imageUrl;
    private String videoUrl;
    private DatabaseReference mDatabaseRef;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        dialog = new ProgressDialog(UploadVideoActivity.this);
        dialog.setCancelable(false);
        dialog.setMessage("Uploading..");

        videoView = findViewById(R.id.uploadvideoview);
        imageView = findViewById(R.id.uploadthumbnail);
        editText = findViewById(R.id.uploadtitle);
        button = findViewById(R.id.uploadbutton);

        mStorageRef = FirebaseStorage.getInstance().getReference("VideoContent");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("VideoContent");

        videoView.setVideoURI(videoUri);
        videoView.setMediaController(new MediaController(UploadVideoActivity.this));
        videoView.canPause();
        videoView.canSeekBackward();
        videoView.canSeekForward();

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                uploadData();
            }
        });


    }


    private void chooseImage() {

        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            imageUri = data.getData();

            Picasso.get().load(imageUri).fit().centerCrop().into(imageView);

        }

    }

    private void uploadData() {

        uploadThumbnail();

    }

    private void uploadThumbnail() {

        if (imageUri != null) {

            final StorageReference fileReference = mStorageRef.child( editText.getText().toString() + System.currentTimeMillis()
                    + "." + getFileExtension(imageUri));

            mUploadTask = fileReference.putFile(imageUri);

            Task<Uri> urlTask = mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        imageUrl = task.getResult().toString();

                        uploadVideo();

                    } else {
                    }
                }
            });

        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void uploadVideo() {

        if (videoUri != null) {

            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(videoUri));

            mUploadTask = fileReference.putFile(videoUri);

            Task<Uri> urlTask = mUploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        videoUrl = task.getResult().toString();

                        uploadFile();

                    } else {
                    }
                }
            });

        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

    }


    private void uploadFile() {

        VideoData data = new VideoData(editText.getText().toString(), FirebaseAuth.getInstance().getUid(),videoUrl,imageUrl);
        String uploadId = mDatabaseRef.push().getKey();
        mDatabaseRef.child(uploadId).setValue(data);
        Toast.makeText(UploadVideoActivity.this, "Success", Toast.LENGTH_SHORT).show();
        dialog.dismiss();
        startActivity(new Intent(UploadVideoActivity.this, FeedsActivity.class));
        finish();
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

}
