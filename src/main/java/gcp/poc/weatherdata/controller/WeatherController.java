package gcp.poc.weatherdata.controller;

import gcp.poc.weatherdata.dto.CurrentWeatherDto;
import gcp.poc.weatherdata.dto.DailyAverage;
import gcp.poc.weatherdata.service.WeatherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/weather")
public class WeatherController {

    private final WeatherService weatherServiceImpl;

    @Autowired
    public WeatherController(WeatherService weatherServiceImpl) {
        this.weatherServiceImpl = weatherServiceImpl;
    }

    @PostMapping(value = "/current")
    public ResponseEntity<CurrentWeatherDto> saveEntry(@Valid @RequestBody CurrentWeatherDto dto) {
        return new ResponseEntity<>(weatherServiceImpl.save(dto), HttpStatus.CREATED);
    }

    @GetMapping(value = "/current")
    public ResponseEntity<CurrentWeatherDto> getLatest() {
        return new ResponseEntity<>(weatherServiceImpl.getLatest(), HttpStatus.OK);
    }

    @GetMapping(value = "/daily-average/{date}")
    public ResponseEntity<DailyAverage> getDailyAverage(@PathVariable String date) {
        return new ResponseEntity<>(weatherServiceImpl.getDailyAverage(date), HttpStatus.OK);
    }

    @GetMapping(value = "/today/{date}")
    public ResponseEntity<List<CurrentWeatherDto>> getTodaysEntries(@PathVariable String date) {
        return new ResponseEntity<>(weatherServiceImpl.getTodaysEntries(date), HttpStatus.OK);
    }
}
