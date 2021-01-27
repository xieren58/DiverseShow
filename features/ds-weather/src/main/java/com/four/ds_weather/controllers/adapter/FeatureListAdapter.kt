package com.four.ds_weather.controllers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.four.ds_weather.R
import com.four.ds_weather.net.WeekWeatherBean

class FeatureListAdapter : RecyclerView.Adapter<FeatureListAdapter.Holder>() {

    private var dataList: List<DataWrapper> = emptyList()

    fun setData(data: List<DataWrapper>) {
        this.dataList = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feature_simple_weather, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val dataWrapper = dataList[position]
        if (dataWrapper.isValid) {
            holder.onBind(position, dataWrapper.data!!)
        } else {
            holder.clear()
        }
    }

    override fun getItemCount(): Int = dataList.size

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDay: TextView = itemView.findViewById(R.id.tvWhichDay)
        private val tvWea: TextView = itemView.findViewById(R.id.tvItemWea)
        private val tvAir: TextView = itemView.findViewById(R.id.tvItemAir)
        private val tvTem: TextView = itemView.findViewById(R.id.tvItemTemperatureScope)

        fun onBind(position: Int, data: WeekWeatherBean.Data) {
            tvDay.text = when(position) {
                0 -> "今天"
                1 -> "明天"
                2 -> "后天"
                else -> "第${position + 1}天"
            }
            tvWea.text = data.wea
            tvTem.text = "${data.tem1}° / ${data.tem2}°"
            tvAir.text = data.air_level
        }

        fun clear() {
            tvDay.text = "-"
            tvWea.text = "-"
            tvTem.text = "-° / -°"
            tvAir.text = "-"
        }
    }

    class DataWrapper(val data: WeekWeatherBean.Data?, val isValid: Boolean)
}