package com.example.cgtaskb.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cgtaskb.common.ValidateHelperImageAdapter
import com.example.cgtaskb.common.getCelsius
import com.example.cgtaskb.data.model.WeatherList
import com.example.cgtaskb.databinding.LayoutForecastBinding

class ForeCastAdapter(val list: List<WeatherList?>) :
    RecyclerView.Adapter<ForeCastAdapter.ForeCastHolder>() {

    private lateinit var binding: LayoutForecastBinding

    inner class ForeCastHolder(val binding: LayoutForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WeatherList) {

            with(binding) {

                tvTime.text = item.dt_txt.substring(11, 16)

                tvTemp.text = getCelsius(item.main.temp)+"°C"

                for (i in item.weather) {
                    ValidateHelperImageAdapter().image(image = i.icon, binding)
                    tvDesc.text = i.description
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForeCastHolder {
        binding =
            LayoutForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ForeCastHolder(binding)
    }

    override fun onBindViewHolder(holder: ForeCastHolder, position: Int) {
        list[holder.absoluteAdapterPosition]?.let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}