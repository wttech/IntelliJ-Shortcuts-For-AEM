package com.cognifide.intellij.shortcuts.newitem.folder;

import com.cognifide.intellij.shortcuts.newitem.validator.NewItemNameValidator;
import com.intellij.ide.actions.CreateElementActionBase;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.psi.PsiDirectory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class NewSlingFolder extends CreateElementActionBase {

    public static final String DIALOG_TEXT = "Sling Folder Name";

    public static final String DIALOG_TITLE = "Create New Sling Folder";

    @NotNull
    @Override
    protected PsiElement[] invokeDialog(Project project, PsiDirectory psiDirectory) {
        PsiElement[] result = new PsiElement[0];
        String initialValue = StringUtils.EMPTY;
        String folderName = Messages.showInputDialog(project,
                DIALOG_TEXT,
                DIALOG_TITLE,
                Messages.getQuestionIcon(),
                initialValue,
                new NewItemNameValidator(psiDirectory));
        if (StringUtils.isNotEmpty(folderName)) {
            result = create(folderName, psiDirectory);
        }
        return result;
    }

    @NotNull
    @Override
    protected PsiElement[] create(String folderName, PsiDirectory directory) {
        List<PsiElement> elements = new LinkedList<>();
        Application application = ApplicationManager.getApplication();
        application.runWriteAction(() -> {
            PsiDirectory folderDirectory = directory.createSubdirectory(folderName);
            elements.add(folderDirectory);
            PsiFile contentXmlFile = folderDirectory.createFile(".content.xml");
            addContentToFile(contentXmlFile);
            elements.add(contentXmlFile);
        });
        return elements.toArray(new PsiElement[elements.size()]);
    }

    @Override
    protected String getErrorTitle() {
        return "Cannot create Sling folder";
    }

    @Override
    protected String getCommandName() {
        return "Create Sling folder";
    }

    @Override
    protected String getActionName(PsiDirectory psiDirectory, String s) {
        return "Create Sling folder";
    }

    private void addContentToFile(PsiFile contentXmlFile) {
        String path = contentXmlFile.getVirtualFile().getPath();
        File file = new File(path);
        try {
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(getSlingFolderContent());
            bw.close();
        } catch (IOException e) {
            Logger.getInstance(getClass().getName()).error(e);
        }
    }

    private String getSlingFolderContent() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<jcr:root xmlns:jcr=\"http://www.jcp.org/jcr/1.0\"\n"
                + "          jcr:mixinTypes=\"[rep:AccessControllable]\"\n"
                + "          jcr:primaryType=\"sling:Folder\"/>";
    }
}