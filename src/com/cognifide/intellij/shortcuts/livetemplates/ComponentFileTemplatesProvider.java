package com.cognifide.intellij.shortcuts.livetemplates;

import com.intellij.codeInsight.template.impl.DefaultLiveTemplatesProvider;
import org.jetbrains.annotations.Nullable;

public class ComponentFileTemplatesProvider implements DefaultLiveTemplatesProvider{

    @Override
    public String[] getDefaultLiveTemplateFiles() {
        return new String[] {"liveTemplates/ComponentFiles"};
    }

    @Nullable
    @Override
    public String[] getHiddenLiveTemplateFiles() {
        return new String[0];
    }
}
