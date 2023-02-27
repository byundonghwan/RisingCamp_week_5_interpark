package com.byundonghwan.interpark_week5.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.byundonghwan.interpark_week5.databinding.ItemBookBinding
import com.byundonghwan.interpark_week5.databinding.ItemHistoryBinding
import com.byundonghwan.interpark_week5.model.Book
import com.byundonghwan.interpark_week5.model.History

class HistoryAdapter(val historyDeleteClickListener : (String) -> Unit ): ListAdapter<History, HistoryAdapter.HistoryItemViewHolder>(diffUtil){

    inner class HistoryItemViewHolder(private val binding : ItemHistoryBinding) : RecyclerView.ViewHolder(binding.root) {

        // 아이템 바인딩작업.
        fun bind(historymodel : History){
            binding.historyKeywordTextView.text = historymodel.keyword

            // 아이템의 삭제버튼 클릭시 최근 검색어 아이템 삭제.
            binding.historyKeywordDeleteButton.setOnClickListener{
                historyDeleteClickListener(historymodel.keyword.orEmpty())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        return HistoryItemViewHolder(ItemHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        // 리사이클러뷰가 뷰 홀더가 변경되었을 때 바꿀지말지 판단하는 변수
        val diffUtil = object : DiffUtil.ItemCallback<History>(){

            override fun areItemsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: History, newItem: History): Boolean {
                return oldItem.keyword == newItem.keyword
            }

        }
    }


}