package it.sesalab.brunelleschi.entities;

import lombok.Value;

@Value
public class SwComponent {
    protected String qualifiedName;
    protected ComponentType type;
}
