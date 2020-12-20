package com.four.ds_weather.net

data class DayWeatherBean(
    val air: String,
    val air_level: String,
    val air_pm25: String,
    val air_tips: String,
    val alarm: Alarm,
    val aqi: Aqi,
    val city: String,
    val cityEn: String,
    val cityid: String,
    val country: String,
    val countryEn: String,
    val date: String,
    val hours: List<Hour>,
    val humidity: String,
    val pressure: String,
    val sunrise: String,
    val sunset: String,
    val tem: String,
    val tem1: String,
    val tem2: String,
    val update_time: String,
    val visibility: String,
    val wea: String,
    val wea_day: String,
    val wea_day_img: String,
    val wea_img: String,
    val wea_night: String,
    val wea_night_img: String,
    val week: String,
    val win: String,
    val win_meter: String,
    val win_speed: String,
    val zhishu: Zhishu
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
        val co: String,
        val co_desc: String,
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

    data class Hour(
        val aqi: String,
        val aqinum: String,
        val hours: String,
        val tem: String,
        val wea: String,
        val wea_img: String,
        val win: String,
        val win_speed: String
    )

    data class Zhishu(
        val chenlian: Chenlian,
        val chuanyi: Chuanyi,
        val daisan: Daisan,
        val diaoyu: Diaoyu,
        val ganmao: Ganmao,
        val kaiche: Kaiche,
        val liangshai: Liangshai,
        val lvyou: Lvyou,
        val xiche: Xiche,
        val ziwaixian: Ziwaixian
    )

    data class Chenlian(
        val level: String,
        val tips: String
    )

    data class Chuanyi(
        val level: String,
        val tips: String
    )

    data class Daisan(
        val level: String,
        val tips: String
    )

    data class Diaoyu(
        val level: String,
        val tips: String
    )

    data class Ganmao(
        val level: String,
        val tips: String
    )

    data class Kaiche(
        val level: String,
        val tips: String
    )

    data class Liangshai(
        val level: String,
        val tips: String
    )

    data class Lvyou(
        val level: String,
        val tips: String
    )

    data class Xiche(
        val level: String,
        val tips: String
    )

    data class Ziwaixian(
        val level: String,
        val tips: String
    )
}