package com.example.databaseusingroom.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.databaseusingroom.R
import com.example.databaseusingroom.adapter.RecyclerViewAdapter
import com.example.databaseusingroom.databinding.ActivityMainBinding
import com.example.databaseusingroom.db.Subscriber
import com.example.databaseusingroom.db.SubscriberDatabase
import com.example.databaseusingroom.db.SubscriberRepository
import com.example.databaseusingroom.viewmodel.MainActivityViewModel
import com.example.databaseusingroom.viewmodel.MainActivityViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var adapter: RecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        val dao = SubscriberDatabase.getInstance(this).subscriberDao
        val viewModelFactory = MainActivityViewModelFactory(SubscriberRepository(dao))
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainActivityViewModel::class.java)
        binding.myViewModel = viewModel
        binding.lifecycleOwner = this
        initRecyclerView()
        displayList()

        viewModel.message.observe(this, {
            it.getContentIfNotHandled()?.let { msg->
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
            }
        })
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun displayList(){
        viewModel.subscriberList.observe(this@MainActivity, {
            Log.i("MyTag", it.toString())
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun initRecyclerView(){
        binding.apply {
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerView.setHasFixedSize(true)
            adapter = RecyclerViewAdapter { selectedSubscriber: Subscriber ->
                listItemClicked(
                    selectedSubscriber
                )
            }
            recyclerView.adapter = adapter
        }
    }

    private fun listItemClicked(selectedSubscriber: Subscriber){
        //Toast.makeText(this, selectedSubscriber.name, Toast.LENGTH_LONG).show()
        viewModel.listItemClick(selectedSubscriber)
    }

}

//Understand coroutines
//CoroutineScope(IO).launch() {
//    Log.i("Hello", "started")
//    val a = async {
//        delay(2000)
//        Log.i("Hello", "work1 from "+ Thread.currentThread().name)
//    }
//    val b = async {
//        delay(4000)
//        Log.i("Hello", "work2 from "+ Thread.currentThread().name)
//    }
//    a.await()
//    b.await()
//    Log.i("Hello", "ended")
//
//}