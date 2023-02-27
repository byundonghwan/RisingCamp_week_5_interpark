package com.byundonghwan.interpark_week5

import android.app.Activity
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import androidx.room.Room
import com.bumptech.glide.Glide
import com.byundonghwan.interpark_week5.databinding.ActivityDetailBinding
import com.byundonghwan.interpark_week5.model.Book
import com.byundonghwan.interpark_week5.model.Review
import java.io.Serializable

class DetailActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailBinding // 뷰 바인딩 변수 선언.
    private lateinit var db: AppDatabase // 최근 검색어를 저장할 local Room db 변수 설정.

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Room DB
        db = getAppDatabase(this)

        // 메인액티비티에서 도서 정보를 받아옴.
        //val model = getSerializable(intent, "object", Book::class.java)
        //Log.d("DetailActivity", model.toString())
        val id = intent.getLongExtra("id",0)
        val title = intent.getStringExtra("title")
        val description = intent.getStringExtra("description")
        val coversmallurl = intent.getStringExtra("coversmallurl")
        Log.d("DetailActivity", id.toString())
        Log.d("DetailActivity", title.toString())
        Log.d("DetailActivity", description.toString())
        Log.d("DetailActivity", coversmallurl.toString())

        // 도서 상세정보 바인딩 작업.
        //binding.titleTextView.text = model?.title.orEmpty()
        //binding.descriptionTextView.text = model?.description.orEmpty()
        binding.titleTextView.text = title.orEmpty()
        binding.descriptionTextView.text = description.orEmpty()

        // Glide를 이용하여 이미지 로딩.
        //Glide.with(binding.coverImageView.context).load(model?.coverSmallUrl.orEmpty()).into(binding.coverImageView)
        Glide.with(binding.coverImageView.context).load(coversmallurl.orEmpty()).into(binding.coverImageView)
        // 스레드를 이용하여 저장된 리뷰 가져와서 UI에 적용.
//        Thread{
//            val review = db.reviewDao().getOneReview(id.toInt() ?:0)
//            runOnUiThread{
//                binding.reviewEditText.setText(review.review.orEmpty())
//            }
//        }.start()

        // 저장하기 버튼 클릭시 -> ROOM DB에 저장.
        binding.saveButton.setOnClickListener {
//            Thread{
//                db.reviewDao().saveReview(
//                    Review(
//                        id.toInt() ?:0 ,
//                binding.reviewEditText.text.toString())
//                )
//            }.start()
        }
    }

//    private fun <T : Serializable?> getSerializable(intent: Intent, key: String, m_class: Class<T>): T {
//        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
//            intent.getSerializableExtra(key, m_class)!!
//        else
//            intent.getSerializableExtra(key) as T
//    }
}

