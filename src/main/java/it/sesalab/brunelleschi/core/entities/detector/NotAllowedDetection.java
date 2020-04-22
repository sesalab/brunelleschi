package it.sesalab.brunelleschi.core.entities.detector;

public class NotAllowedDetection extends RuntimeException {

    public NotAllowedDetection() {
        this("Detection Operation not allowed");
    }

    public NotAllowedDetection(String message) {
        super(message);
    }
}
