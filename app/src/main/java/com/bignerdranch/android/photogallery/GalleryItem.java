package com.bignerdranch.android.photogallery;

/**
 * Created by jakefost on 5/12/16.
 */
public class GalleryItem {
    private String mCaption;
    private String mId;
    private String mUrl;

    public String getCaption() {
        return mCaption;
    }

    public void setCaption(String caption) {
        mCaption = caption;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

    @Override
    public String toString() {
        return mCaption;
    }
}
