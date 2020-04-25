package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.core.entities.Component;
import lombok.Value;

@Value
public class DependencyDescriptor {
    private Component component;
    private int fanIn;
    private int fanOut;

    public int totalDependencies(){
        return fanIn + fanOut;
    }

    public double getInstability(){
        return (double)fanOut/(double) totalDependencies();
    }
}
