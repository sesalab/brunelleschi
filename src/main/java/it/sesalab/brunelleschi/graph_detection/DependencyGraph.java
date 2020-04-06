package it.sesalab.brunelleschi.graph_detection;

import it.sesalab.brunelleschi.entities.SwComponent;

import java.util.List;

public abstract class DependencyGraph {

    public abstract List<List<SwComponent>> getCycles();

}
