package com.progrema.supersave.util;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.database.Cursor;

import java.lang.ref.WeakReference;

/**
 * Created by aria on 11/22/2014.
 */
public class SupersaveAsynchQueryHandler extends AsyncQueryHandler{

    private WeakReference<AsyncQueryListener> mListener;

    /**
     * Interface to listen for completed query operations.
     */
    public interface AsyncQueryListener {
        void onQueryComplete(int token, Object cookie, Cursor cursor);
    }

    public SupersaveAsynchQueryHandler(Context context) {
        super(context.getContentResolver());
    }

    public SupersaveAsynchQueryHandler(Context context, AsyncQueryListener listener) {
        super(context.getContentResolver());
        setQueryListener(listener);
    }

    /**
     * Assign the given {@link AsyncQueryListener} to receive query events from
     * asynchronous calls. Will replace any existing listener.
     */
    public void setQueryListener(AsyncQueryListener listener) {
        mListener = new WeakReference<AsyncQueryListener>(listener);
    }

    @Override
    protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
        final AsyncQueryListener listener = mListener.get();
        if (listener != null) {
            listener.onQueryComplete(token, cookie, cursor);
        } else if (cursor != null) {
            cursor.close();
        }
    }
}
