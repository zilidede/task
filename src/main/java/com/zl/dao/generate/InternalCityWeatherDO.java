package com.zl.dao.generate;

import java.util.Date;

/**
 * @Description:
 * @Param:
 * @Auther: zl
 * @Date: 2023-02-08
 */
public class InternalCityWeatherDO {
    private String nightMeteorological;
    private String dayMeteorological;
    private String dayWind;
    private Date recordTime;
    private Integer minAirTemperature;
    private String cityName;
    private String nightWind;
    private Integer maxAirTemperature;

    public String getNightMeteorological() {
        return nightMeteorological;
    }

    public void setNightMeteorological(String nightMeteorological) {
        this.nightMeteorological = nightMeteorological;
    }

    public String getDayMeteorological() {
        return dayMeteorological;
    }

    public void setDayMeteorological(String dayMeteorological) {
        this.dayMeteorological = dayMeteorological;
    }

    public String getDayWind() {
        return dayWind;
    }

    public void setDayWind(String dayWind) {
        this.dayWind = dayWind;
    }

    public Date getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(Date recordTime) {
        this.recordTime = recordTime;
    }

    public Integer getMinAirTemperature() {
        return minAirTemperature;
    }

    public void setMinAirTemperature(Integer minAirTemperature) {
        this.minAirTemperature = minAirTemperature;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getNightWind() {
        return nightWind;
    }

    public void setNightWind(String nightWind) {
        this.nightWind = nightWind;
    }

    public Integer getMaxAirTemperature() {
        return maxAirTemperature;
    }

    public void setMaxAirTemperature(Integer maxAirTemperature) {
        this.maxAirTemperature = maxAirTemperature;
    }

}
