package io.developersguild.rebelinvader.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import io.developersguild.rebelinvader.RebelInvader;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 800);
        }

        @Override
        public ApplicationListener getApplicationListener() {
                return new RebelInvader();
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new RebelInvader();
        }
}