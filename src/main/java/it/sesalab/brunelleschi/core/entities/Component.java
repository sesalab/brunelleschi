package it.sesalab.brunelleschi.core.entities;

import lombok.Value;

@Value
public class Component {
    protected String qualifiedName;
    protected ComponentType type;
    protected boolean isAbstraction;

    public Component(String qualifiedName, ComponentType type, boolean isAbstraction) {
        this.qualifiedName = qualifiedName;
        this.type = type;
        this.isAbstraction = isAbstraction;
    }

    public Component(String qualifiedName, ComponentType type) {
        this(qualifiedName,type,false);
    }
}
