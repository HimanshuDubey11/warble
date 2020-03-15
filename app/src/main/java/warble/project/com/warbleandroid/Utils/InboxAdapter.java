package warble.project.com.warbleandroid.Utils;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import warble.project.com.warbleandroid.R;

public class InboxAdapter extends RecyclerView.Adapter<InboxAdapter.InboxData> {

    @NonNull
    @Override
    public InboxData onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.activity_inbox_adapter,null);
        return new InboxData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InboxData inboxData, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class InboxData extends RecyclerView.ViewHolder {
        public InboxData(@NonNull View itemView) {
            super(itemView);
        }
    }
}
