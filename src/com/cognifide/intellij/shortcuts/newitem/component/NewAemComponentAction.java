package com.cognifide.intellij.shortcuts.newitem.component;

import com.cognifide.intellij.shortcuts.newitem.validator.NewItemNameValidator;
import com.intellij.ide.actions.CreateElementActionBase;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;

public class NewAemComponentAction extends CreateElementActionBase {

    public static final String DIALOG_TEXT = "AEM Component Name";

    public static final String DIALOG_TITLE = "Create New AEM Component";

    @NotNull
    @Override
    protected PsiElement[] invokeDialog(Project project, PsiDirectory psiDirectory) {
        PsiElement[] result = new PsiElement[0];
        String initialValue = StringUtils.EMPTY;
        String componentName = Messages.showInputDialog(project,
                DIALOG_TEXT,
                DIALOG_TITLE,
                Messages.getQuestionIcon(),
                initialValue,
                new NewItemNameValidator(psiDirectory));
        if (StringUtils.isNotEmpty(componentName)) {
            result = create(componentName, psiDirectory);
        }
        return result;
    }

    @NotNull
    @Override
    protected PsiElement[] create(String componentName, PsiDirectory directory) {
        List<PsiElement> elements = new LinkedList<>();
        Application application = ApplicationManager.getApplication();
        application.runWriteAction(() -> {
            PsiDirectory componentDirectory = directory.createSubdirectory(componentName);
            elements.add(componentDirectory);
            String[] fileNames = getRequiredFileNames(componentName);
            for (String fileName : fileNames) {
                PsiFile file = componentDirectory.createFile(fileName);
                elements.add(file);
            }
        });
        return elements.toArray(new PsiElement[elements.size()]);
    }

    @Override
    protected String getErrorTitle() {
        return "Cannot create AEM component";
    }

    @Override
    protected String getCommandName() {
        return "Create AEM Component";
    }

    @Override
    protected String getActionName(PsiDirectory psiDirectory, String s) {
        return "Create AEM Component";
    }

    @NotNull
    private String[] getRequiredFileNames(String componentName) {
        return new String[]{
                componentName + ".html",
                "_cq_dialog.xml",
                "_cq_editConfig.xml",
                "_cq_template.xml",
                "dialog.xml",
                ".content.xml"};
    }
}
