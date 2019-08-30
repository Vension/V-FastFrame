package com.vension.fastframe.module_news.ui.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vension.fastframe.module_news.R;
import com.vension.fastframe.module_news.bean.VideoNewsModel;

import java.util.LinkedHashMap;
import java.util.List;

import cn.jzvd.JZDataSource;
import cn.jzvd.JzvdStd;
import kv.vension.fastframe.image.glide.GlideProxy;

/**
 * Created by dingcl.
 * 视频数据装配
 */
public class VideoNewsAdapter extends BaseQuickAdapter<VideoNewsModel.Articles, BaseViewHolder> {
    private Context mContext;
//    private  VideoPlayerController controller;

    public VideoNewsAdapter(int layoutResId, List<VideoNewsModel.Articles> data) {
        super(layoutResId, data);
    }

    public VideoNewsAdapter(Context context, int layoutResId, List<VideoNewsModel.Articles> data) {
        super(layoutResId, data);
        mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoNewsModel.Articles articles) {
        int adapterPosition = helper.getAdapterPosition();
        JzvdStd mVideoPlayer = helper.getView(R.id.videoplayers);
        LinearLayout llVideoPlayer = helper.getView(R.id.llVideoPlayer);
        // 将列表中的每个视频设置为默认16:9的比例
        ViewGroup.LayoutParams params = mVideoPlayer.getLayoutParams();
        // 宽度为屏幕宽度
        params.width = llVideoPlayer.getResources().getDisplayMetrics().widthPixels;
        // 高度为宽度的9/16
        params.height = (int) (params.width * 9f / 16f);
        mVideoPlayer.setLayoutParams(params);
        bindData(mVideoPlayer,articles);
        helper.setText(R.id.videoChannel, articles.mediaName == null ? "" : articles.mediaName);
        helper.setText(R.id.tvVideoCommentNum, articles.commentNum +"");

//        ExpandImageView imgTop = helper.getView(R.id.imgTop);
//        TextView tvBottom = helper.getView(R.id.tvBottom);
//        TextView tvStatus = helper.getView(R.id.tvStatus);
//        LinearLayout llJoin =  helper.getView(R.id.llJoin);
//        TextView tvGUBINum =  helper.getView(R.id.tvGUBINum);
//        TextView tvFlagJoinNum =  helper.getView(R.id.tvFlagJoinNum);
//        imgTop.setImageURI(articles.pics.get(0) == null ? "" : articles.pics.get(0).url);
    }




    private void bindData(JzvdStd mVideoPlayer, VideoNewsModel.Articles articles) {
        if (mVideoPlayer != null && articles.videos.size() > 0) {
            //播放带清晰度的视频
            LinkedHashMap map = new LinkedHashMap();
            map.put("高清", articles.videos.get(0).url);
            map.put("普清", articles.videos.get(0).definitionInfos.size() >= 2 ? articles.videos.get(0).definitionInfos.get(0).url : articles.videos.get(0).url);
            map.put("标清", articles.videos.get(0).definitionInfos.size() >= 2 ? articles.videos.get(0).definitionInfos.get(1).url : articles.videos.get(0).url);
            JZDataSource jzDataSource = new JZDataSource(map, articles.title == null ? "" : articles.title);
            jzDataSource.looping = true;
            jzDataSource.currentUrlIndex = 2;
            jzDataSource.headerMap.put("key", "value");//header
            mVideoPlayer.setUp(jzDataSource, JzvdStd.SCREEN_WINDOW_NORMAL);
//            mVideoPlayer.setUp(articles.videos.get(0).url, articles.title == null ? "" : articles.title,Jzvd.SCREEN_WINDOW_NORMAL);
            TextView titleTextView = mVideoPlayer.titleTextView;
            titleTextView.setTextSize(13f);
            titleTextView.setMaxLines(2);
            titleTextView.setEllipsize(TextUtils.TruncateAt.END);
            //粗体
            titleTextView .setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            titleTextView.setPadding(0,10,0,10);
//            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) titleTextView.getLayoutParams();
//            p.setMargins(10,50,10,10);
//            titleTextView.requestLayout();
//            titleTextView.setLayoutParams(p);
        }
        mVideoPlayer.thumbImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        // 将列表中的每个视频设置为默认16:9的比例
        ViewGroup.LayoutParams params = mVideoPlayer.getLayoutParams();
        // 宽度为屏幕宽度
        params.width = mContext.getResources().getDisplayMetrics().widthPixels;
        // 高度为宽度的9/16
        params.height = (int) (params.width * 9f / 16f);
        mVideoPlayer.setLayoutParams(params);
        GlideProxy.with(mContext).load(articles.pics.get(0).url).into(mVideoPlayer.thumbImageView);
    }

}
