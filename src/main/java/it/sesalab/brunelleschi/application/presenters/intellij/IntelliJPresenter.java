package it.sesalab.brunelleschi.application.presenters.intellij;

import it.sesalab.brunelleschi.application.presenters.AbstractPresenterDecorator;
import it.sesalab.brunelleschi.application.presenters.FindSmellPresenter;
import it.sesalab.brunelleschi.core.entities.ArchitecturalSmell;

import java.util.Collection;

public class IntelliJPresenter extends AbstractPresenterDecorator {

    public IntelliJPresenter(FindSmellPresenter wrappedPresenter) {
        super(wrappedPresenter);
    }

    @Override
    public void present(Collection<ArchitecturalSmell> smells) {
        wrappedPresenter.present(smells);
        ResultsDialog dialog = new ResultsDialog(smells);
        dialog.pack();
        dialog.setVisible(true);
    }
}
