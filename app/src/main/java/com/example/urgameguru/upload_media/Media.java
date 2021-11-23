package com.example.urgameguru.upload_media;

public class Media {
    private String mediaName;
    private String mediaUri;

    private Media() {}

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getMediaUri() {
        return mediaUri;
    }

    public void setMediaUri(String mediaUri) {
        this.mediaUri = mediaUri;
    }

    public Media(String name, String uri) {
        if (name.trim().equals("")) {
            name = "Not available";
        }

        mediaName = name;
        mediaUri = uri;
    }
}
