package com.example.urgameguru;

import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewTreeObserver;


public final class FirstDrawListener implements ViewTreeObserver.OnDrawListener {

    private final Handler mainHandler;
    private final View view;
    private final OnFirstDrawCallback firstDrawCallback;

    private boolean onDrawInvoked;

    /**
     * Interface definition for a callback to be invoked for the draw callbacks on the First frame.
     */
    public interface OnFirstDrawCallback {
        /**
         * Callback to be invoked when the first frame is about to be drawn on the Screen.
         * At this time the complete UI (including all views in the view hierarchy) have been
         * measured, laid out and given a frame.
         * <p>
         * Most of the time until this callback is greatly affected by developer's code.
         */
        void onDrawingStart();

        /**
         * Callback to be invoked when the first frame has finished drawing.
         * At this time the complete UI is visible to the user for the first time.
         * <p>
         * Frame rendering is governed by the Android rendering pipeline and so the time difference
         * b/w onDrawingStart() and onDrawingFinish() is greatly effected by the device hardware.
         */
        void onDrawingFinish();
    }

    /**
     * Register a FirstDrawListener instance to the caller.
     *
     * @param view              for which to register the {@link OnFirstDrawCallback}.
     * @param firstDrawCallback to be invoked on various stages of drawing of First frame.
     */
    public static FirstDrawListener registerFirstDrawListener(View view, OnFirstDrawCallback firstDrawCallback) {
        return new FirstDrawListener(view, firstDrawCallback);
    }

    /**
     * Constructor for the class.
     *
     * @param view              for which to register the {@link OnFirstDrawCallback}.
     * @param firstDrawCallback to be invoked on various stages of drawing of First frame.
     */
    private FirstDrawListener(View view, OnFirstDrawCallback firstDrawCallback) {
        super();

        this.view = view;
        this.firstDrawCallback = firstDrawCallback;
        this.mainHandler = new Handler(Looper.getMainLooper());

        registerFirstDrawListener();
    }

    private void registerFirstDrawListener() {
        if (view.getViewTreeObserver().isAlive() && view.isAttachedToWindow()) {
            view.getViewTreeObserver().addOnDrawListener(FirstDrawListener.this);

        } else {
            // Workaround for a bug fixed in API 26
            // https://android.googlesource.com/platform/frameworks/base/+/9f8ec54244a5e0343b9748db3329733f259604f3
            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {

                @Override
                public void onViewAttachedToWindow(View v) {
                    if (view.getViewTreeObserver().isAlive()) {
                        view.getViewTreeObserver().addOnDrawListener(FirstDrawListener.this);
                    }

                    // We only want to listen to this event for the first time only
                    view.removeOnAttachStateChangeListener(this);
                }

                @Override
                public void onViewDetachedFromWindow(View v) {
                    // No-op
                }
            });
        }
    }

    @Override
    public void onDraw() {
        if (!onDrawInvoked) {
            onDrawInvoked = true;

            // Report first draw start
            firstDrawCallback.onDrawingStart();

            // As part of the frame draw the Android Choreographer (coordinates the timing of
            // animations, input and drawing) enqueues a MSG_DO_FRAME message on the
            // Message Queue of the main thread. Processing of that frame and traversal (including
            // the measure pass, layout pass and finally the draw pass) all happens in just one
            // MSG_DO_FRAME message. So coming to this onDraw() callback means that the MSG_DO_FRAME
            // message has been currently processing. Since the Handler processes messages from the
            // Message Queue in a serial fashion we can detect when the drawing finishes by posting
            // a message to the front of Message Queue. When that message is processed by the
            // Handler we can safely assume that the drawing has just been finished completely.
            mainHandler.postAtFrontOfQueue(firstDrawCallback::onDrawingFinish);

            // Remove the listener after the call is finished
            mainHandler.post(() -> {
                if (view.getViewTreeObserver().isAlive()) {
                    view.getViewTreeObserver().removeOnDrawListener(FirstDrawListener.this);
                }
            });
        }
    }
}