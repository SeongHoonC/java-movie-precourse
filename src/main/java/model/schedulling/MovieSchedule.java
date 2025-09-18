package model.schedulling;

import model.common.Auditorium;
import model.common.Movie;
import model.common.Screening;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MovieSchedule {
    
    private final Map<Auditorium, List<Screening>> schedules;
    
    public MovieSchedule() {
        this.schedules = new HashMap<>();
    }
    
    public MovieSchedule(Map<Auditorium, List<Screening>> schedules) {
        this.schedules = new HashMap<>(schedules);
    }

    public void addScreenings(Auditorium auditorium, List<Screening> screenings) {
        schedules.computeIfAbsent(auditorium, k -> new java.util.ArrayList<>()).addAll(screenings);
    }
    
    public List<Screening> getScreenings(Auditorium auditorium) {
        return schedules.getOrDefault(auditorium, new java.util.ArrayList<>());
    }

    public List<Screening> getScreeningByMovie(Movie movie) {
        return schedules.values().stream()
                .flatMap(List::stream)
                .filter(screening -> screening.movie().equals(movie))
                .collect(Collectors.toList());
    }
    
    public Map<Auditorium, List<Screening>> getAllSchedules() {
        return schedules.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new java.util.ArrayList<>(entry.getValue())
                ));
    }
    
    public boolean isEmpty() {
        return schedules.isEmpty();
    }
    
    public void clear() {
        schedules.clear();
    }
}
