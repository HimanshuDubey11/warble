package warble.project.com.warbleandroid.Activities;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import warble.project.com.warbleandroid.Fragments.FeedsFragment;
import warble.project.com.warbleandroid.R;
import warble.project.com.warbleandroid.Utils.VideoData;

public class FeedsActivity extends AppCompatActivity {

    DrawerLayout feedsDrawerLayout;
    ImageView toolbariconImageView;
    BottomNavigationView feedsBottomNavigationView;
    ArrayList<VideoData> myDataArrayList;
    private DatabaseReference mDatabaseRef;

    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;

    //navigationdrawer
    RelativeLayout profileRelativeLayout, settingsRelativeLayout, chatsRelativeLayout, phonemediaRelativeLayout, backedRelativeLayout, logoutRelativeLayout;
    ImageView backButtonNavigationDrawer;
    CircleImageView navigationimageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_feeds);

        feedsDrawerLayout = findViewById(R.id.feedsdrawer);
        toolbariconImageView = findViewById(R.id.feedstoolbaricon);
        feedsBottomNavigationView = findViewById(R.id.bottomnavigator);

        toolbariconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDrawer();
            }
        });

        feedsBottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //navigationDrawer

//        feedsBottomNavigationView.setSelectedItemId(R.id.navigation_home);

        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.feedsframelayout,new FeedsFragment());
        fragmentTransaction.commit();

        profileRelativeLayout = findViewById(R.id.profile);
        settingsRelativeLayout = findViewById(R.id.settings);
        chatsRelativeLayout = findViewById(R.id.chat);
        phonemediaRelativeLayout = findViewById(R.id.phonemedia);
        backedRelativeLayout = findViewById(R.id.backed);
        backButtonNavigationDrawer = findViewById(R.id.backbutton);
        logoutRelativeLayout = findViewById(R.id.logout);

        navigationimageview = findViewById(R.id.navigationimageview);
        Picasso.get().load(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getPhotoUrl()).fit().centerCrop().into(navigationimageview);

        backButtonNavigationDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        profileRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(FeedsActivity.this,ProfileActivity.class));

            }
        });

        chatsRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        phonemediaRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            Intent in=new Intent(getApplicationContext(),PhoneMediaActivity.class);
            startActivity(in);

            }
        });

        backedRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });

        logoutRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(FeedsActivity.this, LoginActivity.class));
            }
        });

    }


    private void openDrawer() {

        feedsDrawerLayout.openDrawer(GravityCompat.START);

    }

    public void onBackPressed() {

        if (feedsDrawerLayout.isDrawerOpen(GravityCompat.START)) {
            feedsDrawerLayout.closeDrawer(GravityCompat.START);
        } else {

            super.onBackPressed();

        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.feedsframelayout,new FeedsFragment());
                    fragmentTransaction.commit();
                    return true;

                case R.id.navigation_dashboard:

                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.feedsframelayout,new InboxActivity());
                    fragmentTransaction.commit();
                    return true;

                case R.id.navigation_notifications:

                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.feedsframelayout,new VerticalVideosActivity());
                    fragmentTransaction.commit();
                    return true;

                case R.id.navigation_discover:

                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.feedsframelayout,new DiscoverActivity());
                    fragmentTransaction.commit();
                    return true;

                case R.id.navigation_profile:

                    fragmentManager = getSupportFragmentManager();
                    fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.feedsframelayout,new ProfileActivity());
                    fragmentTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null) {
            startActivity(new Intent(FeedsActivity.this, LoginActivity.class));
            finish();
        }
    }
}
