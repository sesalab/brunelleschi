package it.sesalab.brunelleschi.core;

public class NotAllowedDetection extends Exception {

    public NotAllowedDetection() {
        this("Detection Operation not allowed");
    }

    public NotAllowedDetection(String message) {
        super(message);
    }
}
