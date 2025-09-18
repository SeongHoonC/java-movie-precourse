package model.schedulling;

import model.common.Auditorium;
import model.common.Movie;
import model.common.OperatingTimes;
import model.common.Screening;

import java.util.List;

public interface SchedulingStrategy {
    List<Screening> schedule(Auditorium auditorium, List<Movie> movies, OperatingTimes operatingTimes);
}
