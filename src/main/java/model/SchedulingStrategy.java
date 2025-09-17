package model;

import java.util.List;

public interface SchedulingStrategy {
    List<Screening> schedule(Auditorium auditorium, List<Movie> movies, OperatingTimes operatingTimes);
}
