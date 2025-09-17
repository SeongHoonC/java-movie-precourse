package model;

import model.rules.DefaultSeatGradeRule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class MovieSchedulerTest {

    private MovieScheduler movieScheduler;
    private Auditorium auditorium;
    private OperatingTimes operatingTimes;

    @BeforeEach
    void setUp() {
        SchedulingStrategy strategy = new GreedySequentialStrategy(30);
        movieScheduler = new MovieScheduler(strategy);
        
        // 테스트용 상영관 생성 (A1~J10 좌석)
        auditorium = Auditorium.of('J', 10, new DefaultSeatGradeRule());
        
        // 운영 시간 설정 (오전 9시 ~ 오후 10시)
        LocalDateTime openTime = LocalDateTime.of(2024, 1, 1, 9, 0);
        LocalDateTime closeTime = LocalDateTime.of(2024, 1, 1, 22, 0);
        operatingTimes = new OperatingTimes(openTime, closeTime);
    }

    @Test
    @DisplayName("단일 영화 상영 스케줄링이 정상적으로 동작한다")
    void scheduleSingleMovie() {
        // given
        Movie movie = new Movie(1, "테스트 영화", 120); // 2시간 영화
        List<Movie> movies = List.of(movie);

        // when
        MovieSchedule movieSchedule = movieScheduler
                .scheduleAuditorium(auditorium, movies, operatingTimes)
                .build();

        // then
        List<Screening> screenings = movieSchedule.getScreenings(auditorium);
        Screening expectedScreening = new Screening(
                null,
                movie,
                LocalDateTime.of(2024, 1, 1, 9, 0),
                LocalDateTime.of(2024, 1, 1, 11, 0),
                auditorium
        );
        assertThat(screenings).containsExactly(expectedScreening);
    }

    @Test
    @DisplayName("여러 영화 상영 스케줄링이 정상적으로 동작한다")
    void scheduleMultipleMovies() {
        // given
        Movie movie1 = new Movie(1, "첫 번째 영화", 90);  // 1시간 30분
        Movie movie2 = new Movie(2, "두 번째 영화", 120); // 2시간
        Movie movie3 = new Movie(3, "세 번째 영화", 100); // 1시간 40분
        List<Movie> movies = List.of(movie1, movie2, movie3);

        // when
        MovieSchedule movieSchedule = movieScheduler
                .scheduleAuditorium(auditorium, movies, operatingTimes)
                .build();

        // then
        List<Screening> screenings = movieSchedule.getScreenings(auditorium);
        List<Screening> expectedScreenings = List.of(
                new Screening(null, movie1, LocalDateTime.of(2024, 1, 1, 9, 0), LocalDateTime.of(2024, 1, 1, 10, 30), auditorium),
                new Screening(null, movie2, LocalDateTime.of(2024, 1, 1, 11, 0), LocalDateTime.of(2024, 1, 1, 13, 0), auditorium),
                new Screening(null, movie3, LocalDateTime.of(2024, 1, 1, 13, 30), LocalDateTime.of(2024, 1, 1, 15, 10), auditorium)
        );
        assertThat(screenings).containsExactlyElementsOf(expectedScreenings);
    }

    @Test
    @DisplayName("동일 영화를 여러번 상영할 때 정상적으로 스케줄링된다")
    void scheduleSameMovieMultipleTimes() {
        List<Movie> movies = createSameMovies(3, 30); // 1시간 40분 영화 5편
        Movie movie = new Movie(1, "반복 영화", 30);

        // when
        MovieSchedule movieSchedule = movieScheduler
                .scheduleAuditorium(auditorium, movies, operatingTimes)
                .build();
        // then 영화 시작 끝 시간 검증
        List<Screening> screenings = movieSchedule.getScreenings(auditorium);
        List<Screening> expectedScreenings = List.of(
                new Screening(null, movie, LocalDateTime.of(2024, 1, 1, 9, 0), LocalDateTime.of(2024, 1, 1, 9, 30), auditorium),
                new Screening(null, movie, LocalDateTime.of(2024, 1, 1, 10, 0), LocalDateTime.of(2024, 1, 1, 10, 30), auditorium),
                new Screening(null, movie, LocalDateTime.of(2024, 1, 1, 11, 0), LocalDateTime.of(2024, 1, 1, 11, 30), auditorium)
        );
        assertThat(screenings).containsExactlyElementsOf(expectedScreenings);
    }

    @Test
    @DisplayName("운영 시간이 정확히 맞는 경우에도 정상적으로 스케줄링된다")
    void scheduleMovieExactlyAtOperatingTime() {
        // given
        // 운영 시간: 9:00 ~ 22:00 (13시간 = 780분)
        // 영화 상영 시간: 120분 + 청소 시간: 30분 = 150분
        Movie movie1 = new Movie(1, "영화1", 120);
        Movie movie2 = new Movie(2, "영화2", 120);
        Movie movie3 = new Movie(3, "영화3", 120);
        Movie movie4 = new Movie(4, "영화4", 120);
        Movie movie5 = new Movie(5, "영화5", 180);
        List<Movie> movies = List.of(movie1, movie2, movie3, movie4, movie5);

        // when & then
        assertThatCode(() -> movieScheduler.scheduleAuditorium(auditorium, movies, operatingTimes).build())
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("영화 상영 시간이 운영 시간을 초과하면 예외가 발생한다")
    void scheduleMovieExceedingOperatingTime() {
        // given
        Movie longMovie = new Movie(1, "긴 영화", 800);
        List<Movie> movies = List.of(longMovie);

        // when & then
        assertThatThrownBy(() -> movieScheduler.scheduleAuditorium(auditorium, movies, operatingTimes).build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("상영할 영화들이 운영 시간을 초과합니다.");
    }

    @Test
    @DisplayName("여러 영화의 총 상영 시간이 운영 시간을 초과하면 예외가 발생한다")
    void scheduleMultipleMoviesExceedingOperatingTime() {
        // given

        List<Movie> movies = createMovies(4,300);

        // when & then
        assertThatThrownBy(() -> movieScheduler.scheduleAuditorium(auditorium, movies, operatingTimes).build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("상영할 영화들이 운영 시간을 초과합니다.");
    }

    @Test
    @DisplayName("여러 영화의 총 상영 시간이 운영 시간을 초과하면 예외가 발생한다")
    void scheduleSameMoviesExceedingOperatingTime() {
        // given

        List<Movie> movies = createMovies(4,300);

        // when & then
        assertThatThrownBy(() -> movieScheduler.scheduleAuditorium(auditorium, movies, operatingTimes).build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("상영할 영화들이 운영 시간을 초과합니다.");
    }


    @Test
    @DisplayName("동일 영화를 여러번 상영할 때 운영 시간을 초과하면 예외가 발생한다")
    void scheduleMovieExceedingOperatingTimeByOneMinute() {
        // given
        // 운영 시간: 9:00 ~ 22:00 (13시간 = 780분)
        List<Movie> movies = createMovies(6, 120);

        // when & then
        assertThatThrownBy(() -> movieScheduler.scheduleAuditorium(auditorium, movies, operatingTimes).build())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("상영할 영화들이 운영 시간을 초과합니다.");
    }

    @Test
    @DisplayName("다양한 상영 시간의 영화들이 정상적으로 스케줄링된다")
    void scheduleMoviesWithVariousRunningTimes() {
        // given
        Movie shortMovie = new Movie(1, "단편 영화", 30);   // 30분
        Movie mediumMovie = new Movie(2, "중편 영화", 90);  // 1시간 30분
        Movie longMovie = new Movie(3, "장편 영화", 150);   // 2시간 30분
        List<Movie> movies = List.of(shortMovie, mediumMovie, longMovie);

        // when
        MovieSchedule movieSchedule = movieScheduler
                .scheduleAuditorium(auditorium, movies, operatingTimes)
                .build();

        // then 영화 시작 끝 시간 검증
        List<Screening> screenings = movieSchedule.getScreenings(auditorium);
        List<Screening> expectedScreenings = List.of(
                new Screening(null, shortMovie, LocalDateTime.of(2024, 1, 1, 9, 0), LocalDateTime.of(2024, 1, 1, 9, 30), auditorium),
                new Screening(null, mediumMovie, LocalDateTime.of(2024, 1, 1, 10, 0), LocalDateTime.of(2024, 1, 1, 11, 30), auditorium),
                new Screening(null, longMovie, LocalDateTime.of(2024, 1, 1, 12, 0), LocalDateTime.of(2024, 1, 1, 14, 30), auditorium)
        );
        assertThat(screenings).containsExactlyElementsOf(expectedScreenings);
    }

    @Test
    @DisplayName("존재하지 않는 상영관의 스케줄을 요청하면 빈 리스트를 반환한다")
    void getScreeningsForNonExistentAuditoriumReturnsEmptyList() {
        // given
        Auditorium anotherAuditorium = Auditorium.of('A', 5, new DefaultSeatGradeRule());

        // when
        MovieSchedule movieSchedule = movieScheduler.build();
        List<Screening> screenings = movieSchedule.getScreenings(anotherAuditorium);

        // then
        assertThat(screenings).isEmpty();
    }

    @Test
    @DisplayName("여러 상영관의 스케줄을 관리할 수 있다")
    void manageMultipleAuditoriumSchedules() {
        // given
        Auditorium auditorium1 = Auditorium.of('A', 5, new DefaultSeatGradeRule());
        Auditorium auditorium2 = Auditorium.of('B', 3, new DefaultSeatGradeRule());
        
        Movie movie1 = new Movie(1, "영화1", 120);
        Movie movie2 = new Movie(2, "영화2", 90);
        
        MovieSchedule movieSchedule = movieScheduler
                .scheduleAuditorium(auditorium1, List.of(movie1), operatingTimes)
                .scheduleAuditorium(auditorium2, List.of(movie2), operatingTimes)
                .build();

        // when
        Map<Auditorium, List<Screening>> allSchedules = movieSchedule.getAllSchedules();
        List<Screening> screenings1 = movieSchedule.getScreenings(auditorium1);
        List<Screening> screenings2 = movieSchedule.getScreenings(auditorium2);

        // then
        assertThat(allSchedules).hasSize(2);
        assertThat(screenings1).hasSize(1);
        assertThat(screenings2).hasSize(1);
    }

    private List<Movie> createMovies(int count, int runningTime) {
        return java.util.stream.IntStream.rangeClosed(1, count)
                .mapToObj(i -> new Movie(i, "영화" + i, runningTime))
                .collect(java.util.stream.Collectors.toList());
    }

    private List<Movie> createSameMovies(int count, int runningTime) {
        Movie movie = new Movie(1, "반복 영화", runningTime);
        return java.util.Collections.nCopies(count, movie);
    }
}
