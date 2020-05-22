package it.sesalab.brunelleschi.application.presenters;

import it.sesalab.brunelleschi.core.entities.ArchitecturalSmell;

import java.util.Collection;

public interface FindSmellPresenter {

    void present(Collection<ArchitecturalSmell> smells);

}
