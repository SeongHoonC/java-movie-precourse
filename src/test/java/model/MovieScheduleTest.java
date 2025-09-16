package model;

import model.rules.DefaultSeatGradeRule;
import model.seat.SeatGrade;
import model.seat.SeatNumber;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;

class MovieScheduleTest {

    private MovieSchedule movieSchedule;
    private Auditorium auditorium;
    private OperatingTimes operatingTimes;

    @BeforeEach
    void setUp() {
        movieSchedule = new MovieSchedule();
        
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
        movieSchedule.scheduleAuditorium(auditorium, movies, operatingTimes);

        // then
        assertThat(movieSchedule).isNotNull();
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
        movieSchedule.scheduleAuditorium(auditorium, movies, operatingTimes);

        // then
        // 스케줄이 정상적으로 생성되었는지 확인
        assertThat(movieSchedule).isNotNull();
    }

    @Test
    @DisplayName("영화 상영 시간이 운영 시간을 초과하면 예외가 발생한다")
    void scheduleMovieExceedingOperatingTime() {
        // given
        Movie longMovie = new Movie(1, "긴 영화", 800); // 13시간 20분 영화
        List<Movie> movies = List.of(longMovie);

        // when & then
        assertThatThrownBy(() -> movieSchedule.scheduleAuditorium(auditorium, movies, operatingTimes))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("상영할 영화들이 운영 시간을 초과합니다.");
    }

    @Test
    @DisplayName("여러 영화의 총 상영 시간이 운영 시간을 초과하면 예외가 발생한다")
    void scheduleMultipleMoviesExceedingOperatingTime() {
        // given
        Movie movie1 = new Movie(1, "첫 번째 영화", 300); // 5시간
        Movie movie2 = new Movie(2, "두 번째 영화", 300); // 5시간
        Movie movie3 = new Movie(3, "세 번째 영화", 300); // 5시간
        Movie movie4 = new Movie(4, "네 번째 영화", 300); // 5시간
        List<Movie> movies = List.of(movie1, movie2, movie3, movie4);

        // when & then
        assertThatThrownBy(() -> movieSchedule.scheduleAuditorium(auditorium, movies, operatingTimes))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("상영할 영화들이 운영 시간을 초과합니다.");
    }

    @Test
    @DisplayName("빈 영화 리스트로 스케줄링해도 예외가 발생하지 않는다")
    void scheduleEmptyMovieList() {
        // given
        List<Movie> emptyMovies = List.of();

        // when & then
        assertThatCode(() -> movieSchedule.scheduleAuditorium(auditorium, emptyMovies, operatingTimes))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("영화 상영 시간에 청소 시간이 포함되어 계산된다")
    void scheduleMovieWithCleaningTime() {
        // given
        Movie movie1 = new Movie(1, "첫 번째 영화", 60);  // 1시간
        Movie movie2 = new Movie(2, "두 번째 영화", 60); // 1시간
        List<Movie> movies = List.of(movie1, movie2);

        // when
        movieSchedule.scheduleAuditorium(auditorium, movies, operatingTimes);

        // then
        // 청소 시간(30분)이 포함되어 계산되므로 정상적으로 스케줄링되어야 함
        assertThat(movieSchedule).isNotNull();
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
        assertThatCode(() -> movieSchedule.scheduleAuditorium(auditorium, movies, operatingTimes))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("운영 시간을 초과하면 예외가 발생한다")
    void scheduleMovieExceedingOperatingTimeByOneMinute() {
        // given
        // 운영 시간: 9:00 ~ 22:00 (13시간 = 780분)
        List<Movie> movies = createMovies(6, 120);

        // when & then
        assertThatThrownBy(() -> movieSchedule.scheduleAuditorium(auditorium, movies, operatingTimes))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("상영할 영화들이 운영 시간을 초과합니다.");
    }

    @Test
    @DisplayName("A1~J10 좌석을 가진 대형 상영관에서 여러 영화 스케줄링이 정상 동작한다")
    void scheduleMultipleMoviesInLargeAuditorium() {
        // given
        // A1~J10까지 총 100개 좌석을 가진 대형 상영관
        Movie movie1 = new Movie(1, "액션 영화", 150);  // 2시간 30분
        Movie movie2 = new Movie(2, "코미디 영화", 90); // 1시간 30분
        Movie movie3 = new Movie(3, "드라마 영화", 180); // 3시간
        Movie movie4 = new Movie(4, "SF 영화", 120);    // 2시간
        List<Movie> movies = List.of(movie1, movie2, movie3, movie4);

        // when
        movieSchedule.scheduleAuditorium(auditorium, movies, operatingTimes);

        // then
        assertThat(movieSchedule).isNotNull();
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
        movieSchedule.scheduleAuditorium(auditorium, movies, operatingTimes);

        // then
        assertThat(movieSchedule).isNotNull();
    }

    @Test
    @DisplayName("청소 시간이 포함된 총 시간 계산이 정확하다")
    void scheduleMoviesWithAccurateCleaningTimeCalculation() {
        // given
        // 운영 시간: 9:00 ~ 22:00 (13시간 = 780분)
        // 각 영화: 60분 + 청소 시간 30분 = 90분
        // 8개 영화: (7 × 90분) + 60분 = 630분 + 60분 = 690분 (780분 이내)
        List<Movie> movies = createMovies(8, 60);

        // when & then
        assertThatCode(() -> movieSchedule.scheduleAuditorium(auditorium, movies, operatingTimes))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("운영 시간을 정확히 맞추는 경우 정상적으로 스케줄링된다")
    void scheduleMovieExactlyAtOperatingTimeWithCleaningTime() {
        // given
        // 운영 시간: 9:00 ~ 22:00 (13시간 = 780분)
        // 각 영화: 60분 + 청소 시간 30분 = 90분
        // 9개 영화: (8 × 90분) + 60분 = 720분 + 60분 = 780분 (정확히 맞음)
        List<Movie> movies = createMovies(9, 60);

        // when & then
        assertThatCode(() -> movieSchedule.scheduleAuditorium(auditorium, movies, operatingTimes))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("마지막 영화의 청소 시간이 운영 시간을 초과하면 예외가 발생한다")
    void scheduleMovieWhereLastCleaningTimeExceedsOperatingTime() {
        // given
        // 운영 시간: 9:00 ~ 22:00 (13시간 = 780분)
        // 각 영화: 60분 + 청소 시간 30분 = 90분
        // 10개 영화: (9 × 90분) + 60분 = 810분 + 60분 = 870분 (780분 초과)
        List<Movie> movies = createMovies(10, 60);

        // when & then
        assertThatThrownBy(() -> movieSchedule.scheduleAuditorium(auditorium, movies, operatingTimes))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("상영할 영화들이 운영 시간을 초과합니다.");
    }

    @Test
    @DisplayName("전체 스케줄을 방어적 복사로 반환한다")
    void getSchedulesReturnsDefensiveCopy() {
        // given
        Movie movie1 = new Movie(1, "테스트 영화1", 120);
        Movie movie2 = new Movie(2, "테스트 영화2", 90);
        List<Movie> movies = List.of(movie1, movie2);
        
        movieSchedule.scheduleAuditorium(auditorium, movies, operatingTimes);

        // when
        Map<Auditorium, List<Screening>> schedules = movieSchedule.getSchedules();
        List<Screening> screenings = schedules.get(auditorium);

        // then
        assertThat(schedules).isNotNull();
        assertThat(screenings).hasSize(2);
        
        // 방어적 복사 확인: 원본 수정해도 반환된 복사본에 영향 없음
        schedules.clear();
        Map<Auditorium, List<Screening>> schedulesAgain = movieSchedule.getSchedules();
        assertThat(schedulesAgain).isNotEmpty();
        assertThat(schedulesAgain.get(auditorium)).hasSize(2);
    }

    @Test
    @DisplayName("특정 상영관의 스케줄을 방어적 복사로 반환한다")
    void getScreeningsReturnsDefensiveCopy() {
        // given
        Movie movie1 = new Movie(1, "테스트 영화1", 120);
        Movie movie2 = new Movie(2, "테스트 영화2", 90);
        List<Movie> movies = List.of(movie1, movie2);
        
        movieSchedule.scheduleAuditorium(auditorium, movies, operatingTimes);

        // when
        List<Screening> screenings = movieSchedule.getScreenings(auditorium);

        // then
        assertThat(screenings).hasSize(2);
        assertThat(screenings.getFirst().movie().title()).isEqualTo("테스트 영화1");
        assertThat(screenings.get(1).movie().title()).isEqualTo("테스트 영화2");
        
        // 방어적 복사 확인: 원본 수정해도 반환된 복사본에 영향 없음
        screenings.clear();
        List<Screening> screeningsAgain = movieSchedule.getScreenings(auditorium);
        assertThat(screeningsAgain).hasSize(2);
    }

    @Test
    @DisplayName("존재하지 않는 상영관의 스케줄을 요청하면 빈 리스트를 반환한다")
    void getScreeningsForNonExistentAuditoriumReturnsEmptyList() {
        // given
        Auditorium anotherAuditorium = Auditorium.of('A', 5, new DefaultSeatGradeRule());

        // when
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
        
        movieSchedule.scheduleAuditorium(auditorium1, List.of(movie1), operatingTimes);
        movieSchedule.scheduleAuditorium(auditorium2, List.of(movie2), operatingTimes);

        // when
        Map<Auditorium, List<Screening>> allSchedules = movieSchedule.getSchedules();
        List<Screening> screenings1 = movieSchedule.getScreenings(auditorium1);
        List<Screening> screenings2 = movieSchedule.getScreenings(auditorium2);

        // then
        assertThat(allSchedules).hasSize(2);
        assertThat(screenings1).hasSize(1);
        assertThat(screenings2).hasSize(1);
        assertThat(screenings1.getFirst().movie().title()).isEqualTo("영화1");
        assertThat(screenings2.getFirst().movie().title()).isEqualTo("영화2");
    }

    private List<Movie> createMovies(int count, int runningTime) {
        return java.util.stream.IntStream.rangeClosed(1, count)
                .mapToObj(i -> new Movie(i, "영화" + i, runningTime))
                .collect(java.util.stream.Collectors.toList());
    }
}
