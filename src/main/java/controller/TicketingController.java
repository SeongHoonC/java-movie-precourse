package controller;

import data.AuditoriumRepository;
import data.MovieRepository;
import model.Ticketing;
import model.common.*;
import model.discount.MovieDayTicketDiscount;
import model.discount.TicketDiscountPolicy;
import model.discount.TicketDiscountPolicyAdapter;
import model.discount.TimeBasedMovieDiscount;
import model.payment.PaymentMachine;
import model.payment.PaymentMethod;
import model.price.MovieDefaultPriceStrategy;
import model.price.MoviePriceStrategy;
import model.schedulling.GreedySequentialStrategy;
import model.schedulling.MovieSchedule;
import model.schedulling.MovieScheduler;
import model.schedulling.SchedulingStrategy;
import model.seat.DefaultSeatGradeRule;

import java.time.LocalDateTime;
import java.util.List;

public class TicketingController {

    public final MovieSchedule movieSchedule = createSchedule(
            LocalDateTime.of(2025, 9, 19, 9, 0),
            LocalDateTime.of(2025, 9, 19, 23, 0)
    );

    private MovieSchedule createSchedule(LocalDateTime open, LocalDateTime close) {
        // 스케줄링
        SchedulingStrategy strategy = new GreedySequentialStrategy(30);
        MovieScheduler scheduler = new MovieScheduler(strategy);
        OperatingTimes times = new OperatingTimes(open, close);

        var auditoriums = AuditoriumRepository.getAuditoriums();
        MovieRepository movieRepo = new MovieRepository();

        //
        List<Movie> movies1 = List.of(
                movieRepo.getMovie(1),
                movieRepo.getMovie(2),
                movieRepo.getMovie(4),
                movieRepo.getMovie(1)
        );

        List<Movie> movies2 = List.of(
                movieRepo.getMovie(3),
                movieRepo.getMovie(4),
                movieRepo.getMovie(5)
        );

        return scheduler.scheduleAuditorium(
                auditoriums.get(0),
                movies1,
                times
        ).scheduleAuditorium(
                auditoriums.get(1),
                movies2,
                times
        ).build();
    }

    public TicketBunch issueTickets(MovieSchedule schedule, List<ScreeningSeat> selections) {
        TicketDiscountPolicy policy = new TicketDiscountPolicyAdapter(
                List.of(new MovieDayTicketDiscount(), new TimeBasedMovieDiscount())
        );
        MoviePriceStrategy priceStrategy = new MovieDefaultPriceStrategy();
        Ticketing ticketing = new Ticketing(schedule, policy, priceStrategy);
        return ticketing.reserveAll(selections);
    }

    public Money pay(TicketBunch bunch, Money point, PaymentMethod method) {
        PaymentMachine machine = new PaymentMachine();
        return machine.calculatePayable(bunch, point, method);
    }
}


