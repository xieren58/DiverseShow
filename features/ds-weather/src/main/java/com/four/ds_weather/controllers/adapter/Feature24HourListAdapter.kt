package com.four.ds_weather.controllers.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.four.ds_weather.R

class Feature24HourListAdapter : RecyclerView.Adapter<Feature24HourListAdapter.Holder>() {

    private var itemWidth = 0

    private var dataList = emptyList<Bean>()

    fun setData(data: List<Bean>) {
        this.dataList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_24_hour_weather, parent, false)
        view.layoutParams = view.layoutParams.apply {
            width = getItemWidth(parent.context)
        }
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        if (position == 0) {
            val lp = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            lp.marginStart = 36
        } else {
            val lp = holder.itemView.layoutParams as ViewGroup.MarginLayoutParams
            lp.marginStart = 0
        }
        holder.onBind(dataList[position])
    }

    override fun getItemCount(): Int = dataList.size

    private fun getItemWidth(context: Context): Int {
        return if (itemWidth != 0) {
            itemWidth
        } else {
            val width = context.resources.displayMetrics.widthPixels
            itemWidth = width / 5 - 10
            itemWidth
        }
    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val tvHour: TextView = itemView.findViewById(R.id.tvHourTime)
        private val tvTem: TextView = itemView.findViewById(R.id.tvHourTem)
        private val tvWea: TextView = itemView.findViewById(R.id.tvHourWea)
        private val tvWind: TextView = itemView.findViewById(R.id.tvHourWind)
        private val tvAir: TextView = itemView.findViewById(R.id.tvHourAir)

        fun onBind(data: Bean) {
            tvHour.text = data.hour
            tvTem.text = data.tem
            tvAir.text = data.air
            tvWind.text = data.wind
            tvWea.text = data.wea
        }
    }

    data class Bean(val hour: String, val tem: String, val wea: String, val wind: String, val air: String)

}