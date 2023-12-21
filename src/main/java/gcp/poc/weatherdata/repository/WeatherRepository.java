package gcp.poc.weatherdata.repository;

import gcp.poc.weatherdata.model.WeatherEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherEntry, Long> {

    @Query("select we from WeatherEntry we order by we.timestamp desc limit 1")
    WeatherEntry findLatest();

    @Query(value = "select * from weather_entry where weather_entry.timestamp like concat(:date, '%')", nativeQuery = true)
    List<WeatherEntry> findAllByTimestamp(@Param("date")String date);
}
