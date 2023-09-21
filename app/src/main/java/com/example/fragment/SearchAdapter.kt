package com.example.fragment

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.fragment.API.getDateFromTimestampWithFormat
import com.example.fragment.databinding.SearchImageBinding
import com.bumptech.glide.Glide

/**
 * 이미지 검색 결과를 표시하는 어댑터 클래스입니다.
 */
class SearchAdapter(private val mContext: Context) : RecyclerView.Adapter<SearchAdapter.ItemViewHolder>() {

    var items = ArrayList<SearchImage>()

    /**
     * 아이템 목록을 초기화하는 메서드입니다.
     */
    fun clearItem() {
        items.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = SearchImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = items[position]

        Glide.with(mContext)
            .load(currentItem.url)
            .into(holder.iv_thum_image)

        holder.iv_like.visibility = if (currentItem.islike) View.VISIBLE else View.INVISIBLE
        holder.tv_title.text = currentItem.title
        holder.tv_datetime.text = getDateFromTimestampWithFormat(
            currentItem.date,
            "yyyy-MM-dd'T'HH:mm:ss.SSS+09:00",
            "yyyy-MM-dd HH:mm:ss"
        )
    }

    override fun getItemCount() = items.size

    inner class ItemViewHolder(binding: SearchImageBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        var iv_thum_image: ImageView = binding.thumbImage
        var iv_like: ImageView = binding.like
        var tv_title: TextView = binding.tvTitle
        var tv_datetime: TextView = binding.tvDate
        var cl_thumb_item: ConstraintLayout = binding.thumblay

        init {
            iv_like.visibility = View.GONE
            iv_thum_image.setOnClickListener(this)
            cl_thumb_item.setOnClickListener(this)
        }

        /**
         * 각 항목 클릭 시 발생하는 이벤트를 처리하는 메서드입니다.
         */
        override fun onClick(view: View) {
            val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
            val item = items[position]

            item.islike = !item.islike

            if (item.islike) {
                (mContext as MainActivity).addLikedItem(item)
            } else {
                (mContext as MainActivity).removeLikedItem(item)
            }

            notifyItemChanged(position)
        }
    }
}
