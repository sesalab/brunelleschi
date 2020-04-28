package it.sesalab.brunelleschi.application;

import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationStarter;
import it.sesalab.brunelleschi.application.actions.StartAction;
import org.jetbrains.annotations.NotNull;

public class CommandLineStarter implements ApplicationStarter {
    @Override
    public String getCommandName() {
        return "brunelleschi";
    }

    @Override
    public void main(@NotNull String[] args) {
        AnAction action = ActionManager.getInstance().getAction("it.sesalab.brunelleschi.application.actions.StartAction");
        AnActionEvent event = new AnActionEvent(null, DataManager.getInstance().getDataContext(),
                ActionPlaces.UNKNOWN, new Presentation(),
                ActionManager.getInstance(), 0);
        action.actionPerformed(event);
    }
}
