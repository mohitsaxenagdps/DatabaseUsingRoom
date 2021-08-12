package com.example.databaseusingroom.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.databaseusingroom.R
import com.example.databaseusingroom.databinding.SubscriberLayoutBinding
import com.example.databaseusingroom.db.Subscriber

class RecyclerViewAdapter(private val clickListener: (Subscriber)->Unit): RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {

    private val subscriberList = ArrayList<Subscriber>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<SubscriberLayoutBinding>(LayoutInflater.from(parent.context), R.layout.subscriber_layout, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(subscriberList[position], clickListener)
    }

    override fun getItemCount() = subscriberList.size

    fun setList(subscribers: List<Subscriber>){
        subscriberList.clear()
        subscriberList.addAll(subscribers)
    }

    class ViewHolder(private val binding: SubscriberLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(subscriber: Subscriber, clickListener: (Subscriber) -> Unit) {
            binding.nameTextView.text = subscriber.name
            binding.emailTextView.text = subscriber.email
            binding.cardView.setOnClickListener{
                clickListener(subscriber)
            }
        }

    }

}