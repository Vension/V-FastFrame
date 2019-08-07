package com.vension.fastframe.app.ui.tab1

import android.graphics.Color
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import android.widget.ViewFlipper
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.vension.fastframe.app.R
import com.vension.fastframe.module_news.utils.GlideImageLoader
import com.youth.banner.Banner
import com.youth.banner.BannerConfig
import com.youth.banner.listener.OnBannerListener
import kv.vension.fastframe.VFrame.getResources
import kv.vension.fastframe.ext.showToast
import kv.vension.fastframe.views.gridpager.GridPager
import lib.vension.fastframe.common.test.TestBean


/**
 * ===================================================================
 * @author: Created by Vension on 2019/8/2 13:08.
 * @email:  250685***4@qq.com
 * @update: update by *** on 2019/8/2 13:08
 * @desc:
 * ===================================================================
 */
class TabAdapter_1(data: MutableList<TestBean>?) : BaseMultiItemQuickAdapter<TestBean, BaseViewHolder>(data) {

    init {
        addItemType(0, R.layout.item_rv_tab_home_banner)
        addItemType(1, R.layout.item_rv_tab_home_viewfilpper)
        addItemType(2, R.layout.item_rv_tab_home_gridmenu)
        addItemType(3, R.layout.item_rv_tab_home_three_menu)
        addItemType(5, R.layout.item_rv_tab_home)
    }

    override fun convert(helper: BaseViewHolder, item: TestBean?) {
        val adapterPosition = helper.adapterPosition
        when (item?.itemType) {
            0 -> {//banner
                bindTypeBanner(helper, item, adapterPosition)
            }
            1 -> {//viewFilpper
                bindTypeViewFilpper(helper, item, adapterPosition)
            }
            2 -> {//网格菜单
                bindTypeGridMenu(helper, item, adapterPosition)
            }
            3 -> {//3图菜单

            }
            5 -> {//普通item

            }
        }
    }


    /**
     * banner 绑定
     */
    private fun bindTypeBanner(helper: BaseViewHolder, item: TestBean, adapterPosition: Int) {
        val mBanner = helper.getView<Banner>(R.id.homeBanner)
        val banners = arrayListOf(R.drawable.banner_1,R.drawable.banner_2,R.drawable.banner_3,R.drawable.banner_4,R.drawable.banner_5)
        //默认是NUM_INDICATOR_TITLE
        mBanner.setImages(banners as MutableList<*>?)
            .setImageLoader(GlideImageLoader())
            .setOnBannerListener(object : OnBannerListener{
                override fun OnBannerClick(position: Int) {
                    showToast("点击了第" + (position + 1) + "张banner")
                }
            })
            .setIndicatorGravity(Gravity.CENTER_HORIZONTAL)
            //设置指示器位置（当banner模式中有指示器时）
            .setIndicatorGravity(BannerConfig.CENTER)
            .setDelayTime(3000)
            .start()
    }

    private fun bindTypeViewFilpper(helper: BaseViewHolder, item: TestBean, adapterPosition: Int) {
        var mStrList = mutableListOf<String>()
        for (i in 1..15) {
            mStrList.add("这是第" + i + "个元素")
        }

        val viewFlipper = helper.getView<ViewFlipper>(R.id.viewFlipper)
        for ((index, str) in mStrList.withIndex()) {
            val textView = TextView(mContext)
            textView.text = str
            if (0 == index % 2) {
                textView.setTextColor(Color.RED)
            } else {
                textView.setTextColor(ContextCompat.getColor(mContext, R.color.color_666666))
            }
            viewFlipper.addView(textView)
        }
    }

    /**
     * gridPage
     */
    private fun bindTypeGridMenu(helper: BaseViewHolder, item: TestBean, adapterPosition: Int) {
        val titles = arrayOf(
            "美食",
            "电影",
            "酒店住宿",
            "休闲娱乐",
            "外卖",
            "自助餐",
            "KTV",
            "机票/火车票",
            "周边游",
            "美甲美睫",
            "火锅",
            "生日蛋糕",
            "甜品饮品",
            "水上乐园",
            "汽车服务",
            "美发",
            "丽人",
            "景点",
            "足疗按摩",
            "运动健身",
            "健身",
            "超市",
            "买菜",
            "今日新单",
            "小吃快餐",
            "面膜",
            "洗浴/汗蒸",
            "母婴亲子",
            "生活服务",
            "婚纱摄影",
            "学习培训",
            "家装",
            "结婚",
            "全部分配"
        )

        val mGridPager = helper.getView<GridPager>(R.id.gridPager)
        mGridPager
            .setDataAllCount(titles.size)
            .setItemBindDataListener(object : GridPager.ItemBindDataListener {
                override fun bindData(imageView: ImageView?, textView: TextView?, position: Int) {
                    // 自己进行数据的绑定，灵活度更高，不受任何限制
                    val imageId = getResources().getIdentifier("ic_category_$position", "drawable", mContext.packageName)
                    imageView!!.setImageResource(imageId)
                    textView!!.text = titles[position]
                }
            })
            // Item点击
            .setGridItemClickListener(object : GridPager.GridItemClickListener {
                override fun onItemClick(position: Int) {
                    showToast("点击了" + titles[position])
                }
            })
            .show()
    }

}