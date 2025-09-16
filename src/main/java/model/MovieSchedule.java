package model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MovieSchedule {

    private final HashMap<Auditorium, List<Screening>> schedules = new HashMap<>();

    public void scheduleAuditorium(Auditorium auditorium, List<Movie> movies, OperatingTimes operatingTimes) {
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

        schedules.put(auditorium, screenings);
    }

    private static void validateOperatingTime(OperatingTimes operatingTimes, LocalDateTime endTime) {
        if (endTime.isAfter(operatingTimes.closeTime())) {
            throw new IllegalStateException(ERROR_MESSAGE);
        }
    }

    public Map<Auditorium, List<Screening>> getSchedules() {
        return schedules.entrySet().stream()
                .collect(java.util.stream.Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> new java.util.ArrayList<>(entry.getValue())
                ));
    }

    public List<Screening> getScreenings(Auditorium auditorium) {
        return java.util.Optional.ofNullable(schedules.get(auditorium))
                .map(java.util.ArrayList::new)
                .orElse(new java.util.ArrayList<>());
    }

    static final int CLEANING_TIME = 30;
    static final String ERROR_MESSAGE = "상영할 영화들이 운영 시간을 초과합니다.";
}


