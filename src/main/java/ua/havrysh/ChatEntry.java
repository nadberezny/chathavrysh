package ua.havrysh;

import java.time.Instant;

public record ChatEntry(int id, String message, String author, Instant createdAt) {

}
