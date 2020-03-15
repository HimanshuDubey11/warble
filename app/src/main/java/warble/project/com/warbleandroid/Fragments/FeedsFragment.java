package warble.project.com.warbleandroid.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import warble.project.com.warbleandroid.Activities.FeedsActivity;
import warble.project.com.warbleandroid.R;
import warble.project.com.warbleandroid.Utils.FeedsAdapter;
import warble.project.com.warbleandroid.Utils.VideoData;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedsFragment extends Fragment {


    public FeedsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feeds, container, false);
    }

    RecyclerView feedsRecyclerView;
    ArrayList<VideoData> myDataArrayList;
    private DatabaseReference mDatabaseRef;
    ProgressBar feedsprogressbar;

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        feedsRecyclerView = view.findViewById(R.id.feedsrecycler);
        feedsprogressbar = view.findViewById(R.id.feedsprogressbar);
        feedsprogressbar.setVisibility(View.VISIBLE);

        myDataArrayList = new ArrayList<VideoData>();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("VideoContent");
//        Query query = mDatabaseRef.orderByChild("userId").equalTo(FirebaseAuth.getInstance().getUid());
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    VideoData mydata = postSnapshot.getValue(VideoData.class);
                    myDataArrayList.add(mydata);
                }
                feedsprogressbar.setVisibility(View.GONE);
                feedsRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext(),LinearLayoutManager.VERTICAL,false));
                feedsRecyclerView.setAdapter(new FeedsAdapter(view.getContext(),myDataArrayList));

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

}
