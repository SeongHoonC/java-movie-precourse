package model;

import java.time.LocalDateTime;
import java.util.List;

public class MovieScheduler {

    private final MovieSchedule movieSchedule = new MovieSchedule();

    public MovieScheduler scheduleAuditorium(Auditorium auditorium, List<Movie> movies, OperatingTimes operatingTimes) {
        LocalDateTime currentTime = operatingTimes.openTime();
        List<Screening> screenings = new java.util.ArrayList<>();

        for (Movie movie : movies) {
            LocalDateTime endTime = currentTime.plusMinutes(movie.runningTime());

            validateOperatingTime(operatingTimes, endTime);

            Screening screening = new Screening(
                    null,
                    movie,
                    currentTime,
                    endTime,
                    auditorium
            );

            screenings.add(screening);
            currentTime = endTime.plusMinutes(CLEANING_TIME);
        }

        movieSchedule.addScreenings(auditorium, screenings);
        return this;
    }

    private static void validateOperatingTime(OperatingTimes operatingTimes, LocalDateTime endTime) {
        if (endTime.isAfter(operatingTimes.closeTime())) {
            throw new IllegalStateException(ERROR_MESSAGE);
        }
    }

    public MovieSchedule build() {
        return movieSchedule;
    }

    static final int CLEANING_TIME = 30;
    static final String ERROR_MESSAGE = "상영할 영화들이 운영 시간을 초과합니다.";
}
