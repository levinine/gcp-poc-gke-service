package gcp.poc.weatherdata.dto;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDateTime;

public class CurrentWeatherDto {

    @Nullable
    private Long id;
    private double temperature;
    @PositiveOrZero
    private double windspeed;
    @PositiveOrZero
    private double winddirection;
    @PastOrPresent
    private LocalDateTime timestamp;

    public CurrentWeatherDto(@Nullable Long id, double temperature, double windspeed, double winddirection, LocalDateTime timestamp) {
        this.id = id;
        this.temperature = temperature;
        this.windspeed = windspeed;
        this.winddirection = winddirection;
        this.timestamp = timestamp;
    }

    public CurrentWeatherDto() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public double getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(double windspeed) {
        this.windspeed = windspeed;
    }

    public double getWinddirection() {
        return winddirection;
    }

    public void setWinddirection(double winddirection) {
        this.winddirection = winddirection;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "CurrentWeatherDto{" +
                "id=" + id +
                ", temperature=" + temperature +
                ", windspeed=" + windspeed +
                ", winddirection=" + winddirection +
                ", timestamp=" + timestamp +
                '}';
    }
}
