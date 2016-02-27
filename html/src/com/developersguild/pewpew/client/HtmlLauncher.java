package com.developersguild.pewpew.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.developersguild.pewpew.PewPew;

public class HtmlLauncher extends GwtApplication {

    @Override
    public GwtApplicationConfiguration getConfig() {
        return new GwtApplicationConfiguration(480, 800);
    }

    @Override
    public ApplicationListener getApplicationListener() {
        return new PewPew();
    }

    @Override
    public ApplicationListener createApplicationListener() {
        return null;
    }
}