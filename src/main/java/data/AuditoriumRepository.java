package data;

import model.Auditorium;
import model.rules.DefaultSeatGradeRule;

import java.util.HashMap;
import java.util.Map;

public class AuditoriumRepository {
    public static Map<Integer, Auditorium> getAuditoriums() {

        HashMap<Integer, Auditorium> auditoriumStore = new HashMap<>();

        auditoriumStore.put(1, Auditorium.of('F', 10, new DefaultSeatGradeRule()));
        auditoriumStore.put(2, Auditorium.of('J', 15, new DefaultSeatGradeRule()));
        auditoriumStore.put(3, Auditorium.of('K', 12, new DefaultSeatGradeRule()));
        auditoriumStore.put(4, Auditorium.of('O', 8, new DefaultSeatGradeRule()));
        auditoriumStore.put(5, Auditorium.of('E', 16, new DefaultSeatGradeRule()));

        return auditoriumStore;
    }
}