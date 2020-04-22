package it.sesalab.brunelleschi.core.entities;

import lombok.Value;

@Value
public class Component {
    protected String qualifiedName;
    protected ComponentType type;
}
