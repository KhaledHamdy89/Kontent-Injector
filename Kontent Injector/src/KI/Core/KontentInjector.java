package KI.Core;

import KI.Models.KITemplateConfiguration;

/**
 * Created by khaled.hamdy on 2/14/17.
 * Copyright (c) 2017 Khaled Hamdy
 */
public class KontentInjector {

    private KITemplateConfiguration currentKIConfig;

    public KontentInjector() {
        KITemplateConfiguration defaultConfig = new KITemplateConfiguration();
    }

    public KontentInjector(KITemplateConfiguration KIConfig) {
    }

    public void configureInjector(KITemplateConfiguration KIConfig) {

    }
}
