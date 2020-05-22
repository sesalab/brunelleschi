package it.sesalab.brunelleschi.application.presenters;

import it.sesalab.brunelleschi.core.usecases.findsmell.FindSmellInteractor;

public abstract class AbstractPresenterDecorator implements FindSmellPresenter {

    protected FindSmellPresenter wrappedPresenter;

    public AbstractPresenterDecorator(FindSmellPresenter wrappedPresenter) {
        this.wrappedPresenter = wrappedPresenter;
    }
}
