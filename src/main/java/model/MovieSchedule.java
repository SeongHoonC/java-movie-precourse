package model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class MovieSchedule {

    HashMap<Auditorium, List<Screening>> schedules= new HashMap<>();

    public void scheduleAuditorium(Auditorium auditorium, List<Movie> movies, OperatingTimes operatingTimes) {
        LocalDateTime currentTime = operatingTimes.openTime();

        for (Movie movie : movies) {
            LocalDateTime endTime = currentTime.plusMinutes(movie.runningTime());
            if (endTime.isAfter(operatingTimes.closeTime())) {
                throw new IllegalStateException("운영 시간을 초과합니다. 상영 영화를 줄여주세요.");
            }
            Screening screening = new Screening(
                    UUID.randomUUID().hashCode(), // 고유 ID 생성
                    movie,
                    currentTime,
                    endTime,
                    auditorium
            );
            schedules.computeIfAbsent(auditorium, k -> new java.util.ArrayList<>()).add(screening);
            currentTime = endTime.plusMinutes(15); // 영화 사이에 15분 휴식 시간 추가
        }
    }

}


