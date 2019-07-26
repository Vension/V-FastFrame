package com.vension.fastframe.module_course.ui.adapter.section

import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.vension.fastframe.module_course.R
import com.vension.fastframe.module_course.bean.VerticalBean
import com.vension.fastframe.module_course.widget.CircleImageView
import com.vension.fastframe.module_course.widget.section.StatelessSection
import com.vension.fastframe.module_course.widget.section.ViewHolder

/**
 * ========================================================
 * 作 者：Vension
 * 日 期：2019/7/26 12:13
 * 更 新：2019/7/26 12:13
 * 描 述：标签item-section
 * ========================================================
 */

class SectionTypeColumn(private val title: String, list: List<VerticalBean.Column.CourseCard>?) : StatelessSection<VerticalBean.Column.CourseCard>(
    R.layout.layout_item_section_type_head, R.layout.layout_item_course_body, list) {

    override fun onBindHeaderViewHolder(holder: ViewHolder) {
        holder.setText(R.id.vertical_text, "一一 $title 一一")
    }

    override fun convert(holder: ViewHolder, mBean: VerticalBean.Column.CourseCard, position: Int) {
        holder.apply {
            mBean.let {
                if (position != 0) getView<ConstraintLayout>(R.id.item).setBackgroundResource(R.drawable.ic_bottom_lines)

                setText(R.id.categoryName, it.categoryName)
                setText(R.id.title, it.courseTitle)
                setText(R.id.courseSaleTime_Num, "开课时间：${it.courseTime} ${it.lessonNum.toInt()}课时")

                if (it.courseSalePrice.toInt() == 0) {
                    setText(R.id.price, "免费")
                    setVisible(R.id.yuan, false)
                } else {
                    setText(R.id.price, "${it.courseSalePrice}")
                }

                if (it.courseSaleNum != 0) setText(R.id.saleNum, "已有${it.courseSaleNum}人购买")

                val ids = intArrayOf(R.id.iv_avatar1, R.id.iv_avatar2, R.id.iv_avatar3)
                val idsName = intArrayOf(R.id.name, R.id.name2, R.id.name3)
                val beanList = it.teacherList
                if (com.vension.fastframe.module_course.util.EmptyUtils.isNotEmpty(beanList)) {
                    beanList.forEachIndexed { index, teacher ->
                        if (index > 2) return
                        Glide.with(mContext)
                                .load(teacher.imgUrl)
                                .centerCrop()
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .dontAnimate()
                                .into(getView<CircleImageView>(ids[index]))
                        getView<TextView>(idsName[index]).text = teacher.name
                    }
                }
            }
        }
    }
}