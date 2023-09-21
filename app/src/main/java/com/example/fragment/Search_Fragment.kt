package com.example.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.fragment.databinding.FragmentSearchBinding
import com.example.fragment.retrofit_client.apiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Search_Fragment : Fragment() {

    private lateinit var binding: FragmentSearchBinding
    private lateinit var mContext: Context
    private lateinit var adapter: SearchAdapter
    private lateinit var gridmanager: StaggeredGridLayoutManager
    private var resItems: ArrayList<SearchImage> = ArrayList()
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        setupViews()   // 뷰 초기 설정
        setupListeners() // 리스너 설정
        return binding.root
    }
    private fun setupViews() {
        // RecyclerView 설정
        gridmanager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.recyclerView.layoutManager = gridmanager

        adapter = SearchAdapter(mContext)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.itemAnimator = null

        // 최근 검색어를 가져와 EditText에 설정
        val lastSearch = API.getLastSearch(requireContext())
        binding.searchEditText.setText(lastSearch)
    }

    private fun setupListeners() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.searchButton.setOnClickListener {
            val query = binding.searchEditText.text.toString()
            if (query.isNotEmpty()) {
                API.saveLastSearch(requireContext(), query)
                adapter.clearItem()
                fetchImageResults(query)
            } else {
                Toast.makeText(mContext, "검색어를 입력해 주세요.", Toast.LENGTH_SHORT).show()
            }

            // 키보드 숨기기
            imm.hideSoftInputFromWindow(binding.searchEditText.windowToken, 0)
        }
    }
    private fun fetchImageResults(query: String) {
        apiService.image_search(API.HEADER, query, "recency", 1, 80)
            ?.enqueue(object : Callback<ImageModel?> {
                override fun onResponse(call: Call<ImageModel?>, response: Response<ImageModel?>) {
                    response.body()?.meta?.let { meta ->
                        if (meta.totalCount > 0) {
                            response.body()!!.documents.forEach { document ->
                                val title = document.displaySitename
                                val datetime = document.datetime
                                val url = document.thumbnailUrl
                                resItems.add(SearchImage(title, datetime, url))
                            }
                        }
                    }
                    adapter.items = resItems
                    adapter.notifyDataSetChanged()
                }

                override fun onFailure(call: Call<ImageModel?>, t: Throwable) {
                    Log.e("#jblee", "onFailure: ${t.message}")
                }
            })
    }
}