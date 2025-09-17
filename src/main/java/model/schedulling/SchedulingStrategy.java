package model.schedulling;

import model.Auditorium;
import model.Movie;
import model.OperatingTimes;
import model.Screening;

import java.util.List;

public interface SchedulingStrategy {
    List<Screening> schedule(Auditorium auditorium, List<Movie> movies, OperatingTimes operatingTimes);
}
