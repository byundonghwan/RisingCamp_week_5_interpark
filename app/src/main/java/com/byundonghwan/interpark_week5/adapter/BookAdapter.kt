package com.byundonghwan.interpark_week5.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.byundonghwan.interpark_week5.databinding.ItemBookBinding
import com.byundonghwan.interpark_week5.model.Book

class BookAdapter(private val itemClickListener: (Book) -> Unit) : ListAdapter<Book, BookAdapter.BookItemViewHolder>(diffUtil){

    inner class BookItemViewHolder(private val binding : ItemBookBinding) : RecyclerView.ViewHolder(binding.root) {

        // 아이템 바인딩작업.
        fun bind(bookModel : Book){
            binding.titleTextView.text = bookModel.title //
            binding.descriptionTextView.text = bookModel.description

            // 아이템 전체 클릭시
            binding.root.setOnClickListener {
                itemClickListener(bookModel) // 메인함수에서 itemClickListener 호출.
            }

            // 이미지 url 형식을 glide를 사용해서 바인딩.
            Glide.with(binding.coverImageView).load(bookModel.coverSmallUrl).into(binding.coverImageView)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookItemViewHolder {
        return BookItemViewHolder(ItemBookBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: BookItemViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        // 리사이클러뷰가 뷰 홀더가 변경되었을 때 바꿀지말지 판단하는 변수
        val diffUtil = object :DiffUtil.ItemCallback<Book>(){

            override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }


}