package gcp.poc.weatherdata.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gcp.poc.weatherdata.dto.CurrentWeatherDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class PubSubMessageResolverService {

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WeatherService weatherService;

    public void resolveMessageAndSave(String message) throws IOException {
        weatherService.save(
        extractRelevantData(mapper.readValue(message.getBytes(), Map.class))
        );
    }

    private CurrentWeatherDto extractRelevantData(Map<String, Object> topLevel) {
        LinkedHashMap<String, Object> currentWeather = (LinkedHashMap<String, Object>) topLevel.get("current_weather");

        CurrentWeatherDto currentWeatherDto = new CurrentWeatherDto();

        currentWeatherDto.setTemperature((double) currentWeather.get("temperature"));
        currentWeatherDto.setWindspeed((double) currentWeather.get("windspeed"));
        currentWeatherDto.setWinddirection((int) currentWeather.get("winddirection"));

        return currentWeatherDto;
    }
}
