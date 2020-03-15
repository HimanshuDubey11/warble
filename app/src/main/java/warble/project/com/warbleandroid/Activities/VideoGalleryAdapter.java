package warble.project.com.warbleandroid.Activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import warble.project.com.warbleandroid.R;

class VideoGalleryAdapter extends BaseAdapter {
    private Context context;
    private List<VideoViewInfo> videoItems;

    LayoutInflater inflater;

    public VideoGalleryAdapter(Context _context,
                               ArrayList<VideoViewInfo> _items) {
        context = _context;
        videoItems = _items;

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getCount() {
        return videoItems.size();
    }

    public Object getItem(int position) {
        return videoItems.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        @SuppressLint({"ViewHolder", "InflateParams"})
        View videoRow = inflater.inflate(R.layout.row, null);
        File videoFile=new File(videoItems.get(position).filePath);
        if (videoFile.exists() && videoFile.length() != 0) {
            //videoThumb.setImageURI(Uri
              //      .parse(videoItems.get(position).thumbPath));
            ImageView videoThumb = (ImageView) videoRow
                    .findViewById(R.id.ImageView);
            videoThumb.setImageBitmap(videoItems.get(position).thumbPath);
            BitmapFactory.Options options=new BitmapFactory.Options();
            options.inSampleSize = 1;
            TextView videoTitle = (TextView) videoRow
                    .findViewById(R.id.TextView);
            videoTitle.setText(videoItems.get(position).title);
            //Bitmap curThumb = MediaStore.Video.Thumbnails.getThumbnail(crThumb, id, MediaStore.Video.Thumbnails.MICRO_KIND, options);
            //iv.setImageBitmap(curThumb);
            //Log.e("Thumbnail at position "+position, "getView: "+Uri.parse(videoItems.get(position).thumbPath));
        }

       /* if(videoItems.get(position).thumbPath==null)
        {
            videoThumb.setBackgroundColor(Color.BLACK);
        }*/

        return videoRow;
    }
}
