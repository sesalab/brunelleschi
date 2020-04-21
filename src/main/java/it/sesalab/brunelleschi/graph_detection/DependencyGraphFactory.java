package it.sesalab.brunelleschi.graph_detection;

public abstract class DependencyGraphFactory {

    protected static final String DEPENDENCY_LABEL = "dependency";
    protected static final String EXTENDS_LABEL = "extends";

    public abstract DependencyGraph makeDependencyGraph();

}
