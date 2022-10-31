package com.example.listnhac.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.listnhac.model.entity.CategoryList
import com.example.listnhac.model.entity.CategoryListItem
import com.example.listnhac.retrofit.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryViewModel: ViewModel() {
    private var categoryLiveData = MutableLiveData<List<CategoryListItem>>()
    fun getCategory(){
        RetrofitInstance.api.getCategory().enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                response.body()?.let {
                    categoryLiveData.postValue(it)
                }
            }
            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.d("Tag", "thất bại")
            }
        })
    }
    fun observeCategory(): LiveData<List<CategoryListItem>> {
        return categoryLiveData
    }
}
