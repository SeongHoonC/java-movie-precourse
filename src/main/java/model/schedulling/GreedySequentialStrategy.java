package model.schedulling;

import model.Auditorium;
import model.Movie;
import model.OperatingTimes;
import model.Screening;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GreedySequentialStrategy implements SchedulingStrategy {
    private final int cleaningMinutes;
    
    public GreedySequentialStrategy(int cleaningMinutes) {
        this.cleaningMinutes = cleaningMinutes;
    }

    @Override
    public List<Screening> schedule(Auditorium auditorium, List<Movie> movies, OperatingTimes operatingTimes) {
        LocalDateTime currentTime = operatingTimes.openTime();
        List<Screening> screenings = new ArrayList<>();
        
        for (Movie movie : movies) {
            LocalDateTime endTime = currentTime.plusMinutes(movie.runningTime());
            
            validateOperatingTime(operatingTimes, endTime);
            
            Screening screening = new Screening(null, movie, currentTime, endTime, auditorium);
            screenings.add(screening);
            currentTime = endTime.plusMinutes(cleaningMinutes);
        }
        
        return screenings;
    }
    
    private static void validateOperatingTime(OperatingTimes operatingTimes, LocalDateTime endTime) {
        if (endTime.isAfter(operatingTimes.closeTime())) {
            throw new IllegalStateException("상영할 영화들이 운영 시간을 초과합니다.");
        }
    }
}
