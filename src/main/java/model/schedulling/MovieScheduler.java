package model.schedulling;

import model.common.Auditorium;
import model.common.Movie;
import model.common.OperatingTimes;
import model.common.Screening;

import java.util.List;

public class MovieScheduler {

    private final MovieSchedule movieSchedule = new MovieSchedule();
    private final SchedulingStrategy schedulingStrategy;

    public MovieScheduler(SchedulingStrategy schedulingStrategy) {
        this.schedulingStrategy = schedulingStrategy;
    }

    public MovieScheduler scheduleAuditorium(Auditorium auditorium, List<Movie> movies, OperatingTimes operatingTimes) {
        List<Screening> screenings = schedulingStrategy.schedule(auditorium, movies, operatingTimes);
        movieSchedule.addScreenings(auditorium, screenings);
        return this;
    }

    public MovieSchedule build() {
        return movieSchedule;
    }
}
