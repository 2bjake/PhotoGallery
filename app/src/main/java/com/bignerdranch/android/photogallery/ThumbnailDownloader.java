package com.bignerdranch.android.photogallery;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;
import android.util.Pair;

import java.io.IOException;

/**
 * Created by jakefost on 5/12/16.
 */
public class ThumbnailDownloader<T> extends HandlerThread {
    private static final String TAG = "ThumbnailDownloader";
    private static final int MESSAGE_DOWNLOAD = 0;

    private boolean mHasQuit;
    private Handler mRequestHandler;
    private Handler mResponseHandler;
    private ThumbnailDownloadListener<T> mThumbnailDownloadListener;

    public interface ThumbnailDownloadListener<T> {
        void onThumbnailDownloaded(T target, Bitmap thumbnail);
    }

    public void setThumbnailDownloadListener(ThumbnailDownloadListener<T> listener) {
        mThumbnailDownloadListener = listener;
    }

    public ThumbnailDownloader(Handler responseHandler) {
        super(TAG);
        mResponseHandler = responseHandler;
    }

    @Override
    protected void onLooperPrepared() {
        mRequestHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    Pair<T, String> msgObj = (Pair<T, String>) msg.obj;
                    T identifier = msgObj.first;
                    String url = msgObj.second;
                    Log.i(TAG, "Got a request for URL: " + msgObj.second);
                    handleRequest(identifier, url);
                }
            }
        };
    }

    private void handleRequest(final T identifier, final String url) {
        try {
            if (url == null) {
                return;
            }

            byte[] bitmapBytes = new FlickrFetchr().getUrlBytes(url);
            final Bitmap bitmap = BitmapFactory
                    .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            Log.i(TAG, "Bitmap created");

            mResponseHandler.post(new Runnable() {

                @Override
                public void run() {
                    if (mHasQuit) {
                        return;
                    }

                    mThumbnailDownloadListener.onThumbnailDownloaded(identifier, bitmap);
                }
            });
        } catch (IOException ioe) {
            Log.e(TAG, "Error downloading image", ioe);
        }
    }

    @Override
    public boolean quit() {
        mHasQuit = true;
        return super.quit();
    }

    public void queueThumbnail(T identifier, String url) {
        Log.i(TAG, "Got a URL: " + url);

        if (url != null) {
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, new Pair<>(identifier, url)).sendToTarget();
        }
    }

    public void clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD);

    }
}
