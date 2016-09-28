package com.cognifide.intellij.shortcuts.newitem.validator;

import com.intellij.openapi.ui.InputValidator;
import com.intellij.psi.PsiDirectory;

public class NewItemNameValidator implements InputValidator {

    private final PsiDirectory destinationDirectory;

    public NewItemNameValidator(PsiDirectory destinationDirectory) {
        this.destinationDirectory = destinationDirectory;
    }

    @Override
    public boolean checkInput(String itemName) {
        return itemName != null && !itemName.isEmpty() && !itemName.contains(" ")
                && fileDoesNotExist(itemName);
    }

    @Override
    public boolean canClose(final String itemName) {
        return checkInput(itemName);
    }

    private boolean fileDoesNotExist(String itemName) {
        boolean fileDoesNotExist = true;
        PsiDirectory[] subdirectories = destinationDirectory.getSubdirectories();
        for (PsiDirectory subdirectory : subdirectories) {
            if (subdirectory.getName().equals(itemName)) {
                fileDoesNotExist = false;
                break;
            }
        }
        return fileDoesNotExist;
    }
}
