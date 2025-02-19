package com.vension.fastframe.module_course.widget.media;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;
import com.vension.fastframe.module_course.R;
import com.vension.fastframe.module_course.widget.media.callback.MediaPlayerListener;
import com.vension.fastframe.module_course.widget.media.callback.VideoBackListener;

import java.util.Locale;
import java.util.Objects;

/**
 * ========================================================
 * 作 者：Vension
 * 日 期：2019/7/25 16:54
 * 更 新：2019/7/25 16:54
 * 描 述：视频控制器
 * ========================================================
 */

public class MediaController extends FrameLayout {

    private static final int sDefaultTimeout = 3000;

    private static final int FADE_OUT = 1;

    private static final int SHOW_PROGRESS = 2;

    private MediaPlayerListener mPlayer;

    private Context mContext;

    private PopupWindow mWindow;

    private int mAnimStyle;

    private View mAnchor;

    private View mRoot;

    private ProgressBar mProgress;

    private TextView mEndTime, mCurrentTime;

    private TextView mTitleView;

    private String mTitle;

    private long mDuration;

    private boolean mShowing;

    private boolean mDragging;

    private boolean mInstantSeeking = true;

    private boolean mFromXml = false;

    private ImageButton mPauseButton;

    private AudioManager mAM;

    private OnShownListener mShownListener;

    private OnHiddenListener mHiddenListener;

    private VideoBackListener mVideoBackListener;

    private ImageView mTvPlay;

    public void setVideoBackEvent(VideoBackListener videoBackListener) {

        this.mVideoBackListener = videoBackListener;
    }


    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            long pos;
            switch (msg.what) {
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_PROGRESS:
                    pos = setProgress();
                    if (!mDragging && mShowing) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                        updatePausePlay();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    private OnClickListener mPauseListener = v -> {
        doPauseResume();
        show(sDefaultTimeout);
    };

    private Runnable lastRunnable;

    private OnSeekBarChangeListener mSeekListener = new OnSeekBarChangeListener() {

        @Override
        public void onStartTrackingTouch(SeekBar bar) {

            mDragging = true;
            show(3600000);
            mHandler.removeMessages(SHOW_PROGRESS);
            if (mInstantSeeking) {
                mAM.setStreamMute(AudioManager.STREAM_MUSIC, true);
            }
        }

        @Override
        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {

            if (!fromuser) {
                return;
            }

            final long newposition = (mDuration * progress) / 1000;
            String time = generateTime(newposition);
            if (mInstantSeeking) {
                mHandler.removeCallbacks(lastRunnable);
                lastRunnable = () -> mPlayer.seekTo(newposition);
                mHandler.postDelayed(lastRunnable, 200);
            }
            if (mCurrentTime != null) {
                mCurrentTime.setText(time);
            }
        }

        @Override
        public void onStopTrackingTouch(SeekBar bar) {

            if (!mInstantSeeking) {
                mPlayer.seekTo((mDuration * bar.getProgress()) / 1000);
            }
            show(sDefaultTimeout);
            mHandler.removeMessages(SHOW_PROGRESS);
            mAM.setStreamMute(AudioManager.STREAM_MUSIC, false);
            mDragging = false;
            mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS, 1000);
        }
    };


    public MediaController(Context context, AttributeSet attrs) {

        super(context, attrs);
        mRoot = this;
        mFromXml = true;
        initController(context);
    }

    public MediaController(Context context) {

        super(context);
        if (!mFromXml && initController(context)) {
            initFloatingWindow();
        }
    }

    private static String generateTime(long position) {

        int totalSeconds = (int) ((position / 1000.0) + 0.5);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes,
                    seconds);
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds);
        }
    }

    private boolean initController(Context context) {

        mContext = context;
        mAM = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        return true;
    }

    @Override
    public void onFinishInflate() {

        super.onFinishInflate();
        if (mRoot != null) {
            initControllerView(mRoot);
        }
    }


    private void initFloatingWindow() {

        mWindow = new PopupWindow(mContext);
        mWindow.setFocusable(false);
        mWindow.setBackgroundDrawable(null);
        mWindow.setOutsideTouchable(true);
        mAnimStyle = android.R.style.Animation;
    }

    /**
     * 设置VideoView
     *
     * @param view
     */
    public void setAnchorView(View view) {

        mAnchor = view;
        if (!mFromXml) {
            removeAllViews();
            mRoot = makeControllerView();
            mWindow.setContentView(mRoot);
            mWindow.setWidth(LayoutParams.MATCH_PARENT);
            mWindow.setHeight(LayoutParams.MATCH_PARENT);
        }
        initControllerView(mRoot);
    }

    /**
     * 创建视图包含小部件,控制回放
     *
     * @return
     */
    protected View makeControllerView() {

        return ((LayoutInflater) Objects.requireNonNull(mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))).inflate(
                R.layout.layout_media_controller, this);
    }

    private void initControllerView(View v) {

        mPauseButton = v.findViewById(R.id.media_controller_play_pause);
        mTvPlay = v.findViewById(R.id.media_controller_tv_play);
        if (mPauseButton != null && mTvPlay != null) {
            mPauseButton.requestFocus();
            mPauseButton.setOnClickListener(mPauseListener);
            mTvPlay.requestFocus();
            mTvPlay.setOnClickListener(v13 -> {
                doPauseResume();
                show(sDefaultTimeout);
            });
        }

        mProgress = (SeekBar) v.findViewById(R.id.media_controller_seekbar);
        if (mProgress != null) {
            SeekBar seeker = (SeekBar) mProgress;
            seeker.setOnSeekBarChangeListener(mSeekListener);
            seeker.setThumbOffset(1);
            mProgress.setMax(1000);
        }

        mEndTime = v.findViewById(R.id.media_controller_time_total);
        mCurrentTime = v.findViewById(R.id.media_controller_time_current);
        mTitleView = v.findViewById(R.id.media_controller_title);
        if (mTitleView != null) {
            mTitleView.setText(mTitle);
        }

        ImageView back = v.findViewById(R.id.media_controller_back);
        back.setOnClickListener(v12 -> mVideoBackListener.back());
    }

    public void setMediaPlayer(MediaPlayerListener player) {

        mPlayer = player;
        updatePausePlay();
    }

    /**
     * 拖动seekBar时回调
     *
     * @param seekWhenDragging
     */
    public void setInstantSeeking(boolean seekWhenDragging) {

        mInstantSeeking = seekWhenDragging;
    }

    public void show() {

        show(sDefaultTimeout);
    }

    /**
     * 设置播放的文件名称
     *
     * @param name
     */
    public void setTitle(String name) {

        mTitle = name;
        if (mTitleView != null) {
            mTitleView.setText(mTitle);
        }
    }

    private void disableUnsupportedButtons() {


        if (mPauseButton != null && mTvPlay != null && !mPlayer.canPause()) {
            mPauseButton.setEnabled(false);
            mTvPlay.setEnabled(false);
        }
    }

    /**
     * 改变控制器的动画风格的资源
     */
    public void setAnimationStyle(int animationStyle) {

        mAnimStyle = animationStyle;
    }

    /**
     * 在屏幕上显示控制器
     *
     * @param timeout
     */
    @SuppressLint("InlinedApi")
    public void show(int timeout) {

        if (!mShowing && mAnchor != null && mAnchor.getWindowToken() != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                mAnchor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
            if (mPauseButton != null && mTvPlay != null) {
                mPauseButton.requestFocus();
                mTvPlay.requestFocus();
            }
            disableUnsupportedButtons();

            if (mFromXml) {
                setVisibility(View.VISIBLE);
            } else {
                int[] location = new int[2];

                mAnchor.getLocationOnScreen(location);
                Rect anchorRect = new Rect(location[0], location[1],
                        location[0] + mAnchor.getWidth(), location[1]
                        + mAnchor.getHeight());

                mWindow.setAnimationStyle(mAnimStyle);
                mWindow.showAtLocation(mAnchor, Gravity.BOTTOM,
                        anchorRect.left, 0);
            }
            mShowing = true;
            if (mShownListener != null) {
                mShownListener.onShow();
            }
        }
        updatePausePlay();
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(mHandler.obtainMessage(FADE_OUT),
                    timeout);
        }
    }

    public boolean isShowing() {

        return mShowing;
    }

    @SuppressLint("InlinedApi")
    public void hide() {

        if (mAnchor == null) {
            return;
        }

        if (mShowing) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                mAnchor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
            try {
                mHandler.removeMessages(SHOW_PROGRESS);
                if (mFromXml) {
                    setVisibility(View.GONE);
                } else {
                    mWindow.dismiss();
                }
            } catch (IllegalArgumentException ex) {
                Log.e("","MediaController already removed");
            }
            mShowing = false;
            if (mHiddenListener != null) {
                mHiddenListener.onHidden();
            }
        }
    }

    public void setOnShownListener(OnShownListener l) {

        mShownListener = l;
    }

    public void setOnHiddenListener(OnHiddenListener l) {

        mHiddenListener = l;
    }

    private long setProgress() {

        if (mPlayer == null || mDragging) {
            return 0;
        }

        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mProgress != null) {
            if (duration > 0) {
                long pos = 1000L * position / duration;
                mProgress.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }

        mDuration = duration;

        if (mEndTime != null) {
            mEndTime.setText(generateTime(mDuration));
        }
        if (mCurrentTime != null) {
            mCurrentTime.setText(generateTime(position));
        }

        return position;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            hide();
        }
        return true;
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(sDefaultTimeout);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {

        int keyCode = event.getKeyCode();
        if (event.getRepeatCount() == 0
                && (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE || keyCode == KeyEvent.KEYCODE_SPACE)) {
            doPauseResume();
            show(sDefaultTimeout);
            if (mPauseButton != null && mTvPlay != null) {
                mPauseButton.requestFocus();
                mTvPlay.requestFocus();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                updatePausePlay();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK
                || keyCode == KeyEvent.KEYCODE_MENU) {
            hide();
            return true;
        } else {
            show(sDefaultTimeout);
        }
        return super.dispatchKeyEvent(event);
    }

    private void updatePausePlay() {

        if (mRoot == null || mPauseButton == null || mTvPlay == null) {
            return;
        }

        if (mPlayer.isPlaying()) {
            mPauseButton.setImageResource(R.drawable.weike_player_play_can_pause);
            mTvPlay.setImageResource(R.drawable.ic_tv_stop);
        } else {
            mPauseButton.setImageResource(R.drawable.weike_player_play_can_play);
            mTvPlay.setImageResource(R.drawable.ic_tv_play);
        }
    }

    private void doPauseResume() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.play();
        }
        updatePausePlay();
    }

    @Override
    public void setEnabled(boolean enabled) {

        if (mPauseButton != null) {
            mPauseButton.setEnabled(enabled);
        }
        if (mTvPlay != null) {
            mTvPlay.setEnabled(enabled);
        }
        if (mProgress != null) {
            mProgress.setEnabled(enabled);
        }
        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    public interface OnShownListener {
        void onShow();

    }

    public interface OnHiddenListener {

        void onHidden();
    }
}
