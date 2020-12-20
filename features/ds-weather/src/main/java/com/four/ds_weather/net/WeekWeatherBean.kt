package com.four.ds_weather.net

data class WeekWeatherBean(
    val aqi: Aqi,
    val city: String,
    val cityEn: String,
    val cityid: String,
    val country: String,
    val countryEn: String,
    val `data`: List<Data>,
    val update_time: String
) {
    data class Alarm(
        val alarm_content: String,
        val alarm_level: String,
        val alarm_type: String
    )

    data class Aqi(
        val air: String,
        val air_level: String,
        val air_tips: String,
        val city: String,
        val cityEn: String,
        val cityid: String,
        val co: String,
        val co_desc: String,
        val country: String,
        val countryEn: String,
        val jinghuaqi: String,
        val kaichuang: String,
        val kouzhao: String,
        val no2: String,
        val no2_desc: String,
        val o3: String,
        val o3_desc: String,
        val pm10: String,
        val pm10_desc: String,
        val pm25: String,
        val pm25_desc: String,
        val so2: String,
        val so2_desc: String,
        val update_time: String,
        val waichu: String,
        val yundong: String
    )

    data class Data(
        val air: String,
        val air_level: String,
        val air_tips: String,
        val alarm: Alarm,
        val date: String,
        val day: String,
        val hours: List<Hour>,
        val humidity: String,
        val index: List<Index>,
        val pressure: String,
        val sunrise: String,
        val sunset: String,
        val tem: String,
        val tem1: String,
        val tem2: String,
        val visibility: String,
        val wea: String,
        val wea_day: String,
        val wea_day_img: String,
        val wea_img: String,
        val wea_night: String,
        val wea_night_img: String,
        val week: String,
        val win: List<String>,
        val win_meter: String,
        val win_speed: String
    )

    data class Hour(
        val hours: String,
        val tem: String,
        val wea: String,
        val wea_img: String,
        val win: String,
        val win_speed: String
    )

    data class Index(
        val desc: String,
        val level: String,
        val title: String
    )
}


