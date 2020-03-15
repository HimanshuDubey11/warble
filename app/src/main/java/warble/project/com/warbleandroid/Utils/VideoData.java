package warble.project.com.warbleandroid.Utils;

import java.io.Serializable;

public class VideoData implements Serializable {

    String videoName;
    String userId;
    String videoLikes;
    String videoViews;
    String videoUrl;
    String videoThumbnail;

    public VideoData() {
    }

    public VideoData(String videoUrl) {
        this.videoUrl = videoUrl;
    }


    public VideoData(String videoName, String userId, String videoLikes, String videoViews, String videoUrl, String videoThumbnail) {
        this.videoName = videoName;
        this.userId = userId;
        this.videoLikes = videoLikes;
        this.videoViews = videoViews;
        this.videoUrl = videoUrl;
        this.videoThumbnail = videoThumbnail;
    }

    public VideoData(String videoName, String userId, String videoUrl, String videoThumbnail) {
        this.videoName = videoName;
        this.userId = userId;
        this.videoUrl = videoUrl;
        this.videoThumbnail = videoThumbnail;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public String getuserId() {
        return userId;
    }

    public void setuserId(String userId) {
        this.userId = userId;
    }

    public String getVideoLikes() {
        return videoLikes;
    }

    public void setVideoLikes(String videoLikes) {
        this.videoLikes = videoLikes;
    }

    public String getVideoViews() {
        return videoViews;
    }

    public void setVideoViews(String videoViews) {
        this.videoViews = videoViews;
    }

    public String getVideoThumbnail() {
        return videoThumbnail;
    }

    public void setVideoThumbnail(String videoThumbnail) {
        this.videoThumbnail = videoThumbnail;
    }
}
