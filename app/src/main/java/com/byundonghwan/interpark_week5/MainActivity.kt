package com.byundonghwan.interpark_week5

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.byundonghwan.interpark_week5.adapter.BookAdapter
import com.byundonghwan.interpark_week5.adapter.HistoryAdapter
import com.byundonghwan.interpark_week5.api.BookService
import com.byundonghwan.interpark_week5.databinding.ActivityMainBinding
import com.byundonghwan.interpark_week5.model.BestCellerDto
import com.byundonghwan.interpark_week5.model.Book
import com.byundonghwan.interpark_week5.model.History
import com.byundonghwan.interpark_week5.model.SearchBookDto
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding // 뷰 바인딩 변수 선언.
    private lateinit var bookAdapter: BookAdapter // 도서정보 리사이클러뷰 어댑터.
    private lateinit var historyAdapter: HistoryAdapter // 최근 검색어 리사이클러뷰 어댑터.
    private lateinit var bookService: BookService // Retrofit BookService인터페이스 변수 선언.
    private lateinit var db: AppDatabase // 최근 검색어를 저장할 local Room db 변수 설정.

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 리사이클러 뷰 메인액티비티에 연결.
        initBookRecyclerView()
        initHistoryRecyclerView()
        initSearchEditText()

        // Room DB
        db = getAppDatabase(this)


        val retrofit = Retrofit.Builder()
            .baseUrl("https://book.interpark.com")
            .addConverterFactory(GsonConverterFactory.create()) // 데이터를 json 형식으로 받아옴.
            .build()

        bookService = retrofit.create(BookService::class.java) // api폴더/BookService인터페이스
        bookService.getBestSellerBooks("8AF8383DA41DDC79CD00675A0C088FDC53268FC18C0A086DAA55FA812147E4D5")
            .enqueue(object : Callback<BestCellerDto> {

                //API 요청 성공시
                override fun onResponse(
                    call: Call<BestCellerDto>,
                    response: Response<BestCellerDto>
                ) {
                    if (response.isSuccessful.not()) {
                        Log.d(TAG, "API요청 실패")
                        return
                    } else {
                        // BestCellerDto의 body를 가져옴.
                        response.body()?.let {
                            Log.d(TAG, "API요청 성공")
                            // body/books 리스트 접근.
                            it.books.forEach { book ->
                                Log.d(TAG, book.toString())
                            }
                            // 리사이클러뷰에 api에서 가져온 books 베스트 셀러 정보를 반영.
                            bookAdapter.submitList(it.books)
                        }
                    }
                }

                // API 요청 실패시
                override fun onFailure(call: Call<BestCellerDto>, t: Throwable) {
                    Log.d(TAG, t.toString())
                }

            })
    }

    // ==================================== 리사이클러뷰 어댑터 구현 함수 ====================================
    private fun initBookRecyclerView() {
        bookAdapter = BookAdapter(itemClickListener = {
            val intent = Intent(this,DetailActivity::class.java)
            intent.putExtra("id", it.id)
            intent.putExtra("title", it.title)
            intent.putExtra("description", it.description)
            intent.putExtra("coversmallurl", it.coverSmallUrl)
            startActivity(intent)
        })
        binding.bookRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.bookRecyclerView.adapter = bookAdapter
    }

    private fun initHistoryRecyclerView() {
        historyAdapter = HistoryAdapter(historyDeleteClickListener = {
            deleteSearchKeyword(it)
        })
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.historyRecyclerView.adapter = historyAdapter
        initSearchEditText()
    }

    // ==================================== 검색창 기능 구현 함수 ====================================
    private fun initSearchEditText() {
        // 검색창 기능 구현.
        binding.searchEditText.setOnKeyListener { v, keyCode, event ->
            //엔터 눌렀을 때 -> 최근 검색 리사이클러뷰 INVISIBLE -> 검색API호출된 데이터 보여줌.
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == MotionEvent.ACTION_DOWN) {
                search(binding.searchEditText.text.toString()) // 입력받은 정보를 검색 API 호출.
                return@setOnKeyListener true
            }
            return@setOnKeyListener false
        }
        // 검색창 클릭 리스너 발생시
        binding.searchEditText.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                showHistoryView()
            }
            return@setOnTouchListener false
        }
    }

    private fun search(keyword: String) {
        // 검색 api 호출.
        bookService.getBooksByName(
            "8AF8383DA41DDC79CD00675A0C088FDC53268FC18C0A086DAA55FA812147E4D5",
            keyword
        )
            .enqueue(object : Callback<SearchBookDto> {
                //API 요청 성공시
                override fun onResponse(
                    call: Call<SearchBookDto>,
                    response: Response<SearchBookDto>
                ) {

                    HideHistoryView() // 최근 검색 리사이클러뷰 INVISIBLE
                    saveSearchKeyword(keyword)  // Room db에 저장.

                    if (response.isSuccessful.not()) {
                        Log.d(TAG, "API요청 실패")
                        return
                    }
                    // 리사이클러뷰에 api에서 가져온 books 검색 정보를 반영.
                    bookAdapter.submitList(response.body()?.books.orEmpty())
                }

                // API 요청 실패시
                override fun onFailure(call: Call<SearchBookDto>, t: Throwable) {
                    Log.d(TAG, t.toString())
                    HideHistoryView() // 최근 검색 리사이클러뷰 INVISIBLE
                }

            })
    }

    // 최근 검색어 Room db에서 아이템 삭제 함수.
    private fun deleteSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().delete(keyword) // 최근 검색어 Room db에서 아이템 삭제.
            showHistoryView() // 갱신.
        }.start()
    }

    // 최근 검색어 Room db에 저장하는 함수.
    private fun saveSearchKeyword(keyword: String) {
        Thread {
            db.historyDao().insertHistory(History(null, keyword))
        }.start()
    }

    // 최근 검색한 데이터 리사이클러뷰 ROOM DB에서 모든 정보 UI에 보여줌.
    private fun showHistoryView() {
        Thread {
            val keywords = db.historyDao().getAll().reversed() // 최근검색한 데이터 ROOM DB에서 모든 정보 가져옴.

            runOnUiThread {
                binding.historyRecyclerView.isVisible = true
                historyAdapter.submitList(keywords.orEmpty())
            }
        }.start()

        binding.historyRecyclerView.isVisible = true
    }

    // 최근 검색어 리사이클러뷰 INVISIBLE 함수.
    private fun HideHistoryView() {
        binding.historyRecyclerView.isVisible = false
    }

    companion object {
        private const val TAG = "MainActivity"

    }


}