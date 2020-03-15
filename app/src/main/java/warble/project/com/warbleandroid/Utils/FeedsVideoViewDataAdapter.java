package warble.project.com.warbleandroid.Utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import warble.project.com.warbleandroid.R;

public class FeedsVideoViewDataAdapter extends RecyclerView.Adapter<FeedsVideoViewDataAdapter.Data> {

    @NonNull
    @Override
    public FeedsVideoViewDataAdapter.Data onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_feeds_video_view_data_adapter,null);

        return new Data(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedsVideoViewDataAdapter.Data data, int i) {

    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class Data extends RecyclerView.ViewHolder {
        public Data(@NonNull View itemView) {
            super(itemView);
        }
    }
}
