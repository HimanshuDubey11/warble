package warble.project.com.warbleandroid.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import warble.project.com.warbleandroid.R;
import warble.project.com.warbleandroid.Utils.FeedsAdapter;
import warble.project.com.warbleandroid.Utils.PrivateVideosAdapter;
import warble.project.com.warbleandroid.Utils.VideoData;

import static android.app.Activity.RESULT_OK;

public class ProfileActivity extends Fragment {

    private static final int MY_PERMISSIONS_REQUEST_ACCESS_CODE = 22;
    private static final int PICK_VIDEO_REQUEST = 20;
    LinearLayout uploadLinearLayout;
    Uri uploadURi;
    RelativeLayout bottomsheet, profile_bottom_sheet;
    TextView Name, Followers, Following, Post, FollowerCount, FollowingCount, PostCount, FollowingText, uploadText;

    BottomSheetBehavior sheetBehavior;

    RecyclerView scrolledRecyclerView;

    CircleImageView peoplesimageback;

    ImageView profileactivitybackground, arrowscrolledup, arrowscrolleddown;


    //extreme
    ArrayList<VideoData> myDataArrayList;
    private DatabaseReference mDatabaseRef;


    private static final String[] requiredPermissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
    };

    public ProfileActivity() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomsheet = view.findViewById(R.id.bottomsheet);
        profile_bottom_sheet = view.findViewById(R.id.profile_bottom_sheet);
        arrowscrolledup = view.findViewById(R.id.arrowscrolledup);
        arrowscrolleddown = view.findViewById(R.id.arrowscrolleddown);

        scrolledRecyclerView = view.findViewById(R.id.scrolledrecycler);

//        changeColor = view.findViewById(R.id.change_text_color);
        Name = view.findViewById(R.id.peopletextname);
        Followers = view.findViewById(R.id.peopletextfollower);
        Following = view.findViewById(R.id.peopletextfollowing);
        Post = view.findViewById(R.id.peopletextpost);
        FollowerCount = view.findViewById(R.id.peoplenumfollower);
        FollowingCount = view.findViewById(R.id.peoplenumfollowing);
        PostCount = view.findViewById(R.id.peoplenumpost);
        FollowingText = view.findViewById(R.id.follow_status);
        uploadText = view.findViewById(R.id.upload_text);


//        peoplesimageback = view.findViewById(R.id.peoplesimageback);

        Name.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getDisplayName());

        sheetBehavior = BottomSheetBehavior.from(profile_bottom_sheet);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        arrowscrolledup.setVisibility(View.GONE);
                        arrowscrolledup.setEnabled(false);
                        arrowscrolleddown.setVisibility(View.VISIBLE);
                        arrowscrolleddown.setEnabled(true);
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        arrowscrolleddown.setVisibility(View.GONE);
                        arrowscrolleddown.setEnabled(false);
                        arrowscrolledup.setVisibility(View.VISIBLE);
                        arrowscrolledup.setEnabled(true);
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {

            }
        });

        checkPermissions();

        uploadLinearLayout = view.findViewById(R.id.uploadlayout);
        uploadLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                chooseVideo();

            }
        });


        profileactivitybackground = view.findViewById(R.id.profileactivitybackground);
        Picasso.get().load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl()).fit().centerCrop().into(profileactivitybackground);

    }

    private void chooseVideo() {

        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            uploadURi = data.getData();

            UploadVideoActivity.videoUri = uploadURi;

            startActivity(new Intent(getContext(), UploadVideoActivity.class));

        }

    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermissions() {
        final List<String> neededPermissions = new ArrayList<>();
        for (final String permission : requiredPermissions) {
            if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(),
                    permission) != PackageManager.PERMISSION_GRANTED) {
                neededPermissions.add(permission);
            }
        }
        if (!neededPermissions.isEmpty()) {
            requestPermissions(neededPermissions.toArray(new String[]{}),
                    MY_PERMISSIONS_REQUEST_ACCESS_CODE);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        myDataArrayList = new ArrayList<VideoData>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("VideoContent");
        Query query = mDatabaseRef.orderByChild("userId").equalTo(FirebaseAuth.getInstance().getUid());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    VideoData mydata = postSnapshot.getValue(VideoData.class);
                    myDataArrayList.add(mydata);
                }
                scrolledRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
                scrolledRecyclerView.setAdapter(new PrivateVideosAdapter(getContext(),myDataArrayList));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }
}
