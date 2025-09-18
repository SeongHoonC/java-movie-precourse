package model.common;

public record Movie(
        int id,
        String title,
        int runningTime // in minutes
) {
}
