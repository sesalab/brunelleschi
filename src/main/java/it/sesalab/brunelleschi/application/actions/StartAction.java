package it.sesalab.brunelleschi.application.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class StartAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        System.out.println("CIAUNZ");
    }
}
