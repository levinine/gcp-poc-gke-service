package gcp.poc.weatherdata.service;

import gcp.poc.weatherdata.dto.CurrentWeatherDto;
import gcp.poc.weatherdata.dto.DailyAverage;
import gcp.poc.weatherdata.model.WeatherEntry;
import gcp.poc.weatherdata.repository.WeatherRepository;
import net.iakovlev.timeshape.TimeZoneEngine;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.TimeZone;

@Service
public class WeatherService {

    private final WeatherRepository weatherRepository;
    private final ModelMapper mapper;

    @Autowired
    public WeatherService(WeatherRepository weatherRepository, ModelMapper mapper) {
        this.weatherRepository = weatherRepository;
        this.mapper = mapper;
    }

    public CurrentWeatherDto save(CurrentWeatherDto weatherEntry) {
        WeatherEntry entry = mapper.map(weatherEntry, WeatherEntry.class);

        // magic values at their best :)
        double LATITUDE = 45.25;
        double LONGITUDE = 19.8125;

        entry.setTimeZone(getTimezone(LATITUDE, LONGITUDE));
        entry.setTimestamp(LocalDateTime.now(getZoneId(LATITUDE, LONGITUDE)));
        entry.setDay(isDay(entry.getTimestamp()));

        entry = weatherRepository.save(entry);

        return mapper.map(entry, CurrentWeatherDto.class);
    }

    public CurrentWeatherDto getLatest() {
        return mapper.map(weatherRepository.findLatest(), CurrentWeatherDto.class);
    }

    public DailyAverage getDailyAverage(String date) {
        List<WeatherEntry> entriesForDate = weatherRepository.findAllByTimestamp(date);
        System.out.println(entriesForDate.size());
        DailyAverage returnValue = new DailyAverage();

        for(WeatherEntry we : entriesForDate) {
            returnValue.setTemperature(returnValue.getTemperature() + we.getTemperature());
            returnValue.setWinddirection(returnValue.getWinddirection() + we.getWindDirection());
            returnValue.setWindspeed(returnValue.getWindspeed() + we.getWindSpeed());
        }

        returnValue.setTemperature(returnValue.getTemperature() / entriesForDate.size());
        returnValue.setWinddirection(returnValue.getWinddirection() / entriesForDate.size());
        returnValue.setWindspeed(returnValue.getWindspeed() / entriesForDate.size());

        entriesForDate.stream().findFirst().ifPresent(entry -> {
            returnValue.setDate(entriesForDate.get(0).getTimestamp().toLocalDate());
        });

        return returnValue;
    }

    public List<CurrentWeatherDto> getTodaysEntries(String date) {
        return weatherRepository.findAllByTimestamp(date)
                .stream()
                .map(weatherEntry -> mapper.map(weatherEntry, CurrentWeatherDto.class)).toList();
    }

    private String getTimezone(double latitude, double longitude) {
        return TimeZone.getTimeZone(
                getEngine()
                        .query(latitude, longitude)
                        .get()
                        .getId())
                .getDisplayName();
    }

    private ZoneId getZoneId(double latitude, double longitude) {
        return ZoneId.of(TimeZone
                .getTimeZone(getEngine()
                        .query(latitude, longitude)
                        .get())
                .getID());
    }

    private boolean isDay(LocalDateTime dateTime) {
        int hrs = Integer.parseInt(dateTime.toString().split("T")[1].split(":")[0]);
        return 6 < hrs && hrs < 20;
    }

    @Cacheable("timeZoneEngineCache")
    public TimeZoneEngine getEngine() {
        return TimeZoneEngine.initialize();
    }
}
