package com.sugarware.gravity.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.sugarware.gravity.GdxGame;

public class HtmlLauncher extends GwtApplication {

		ApplicationListener appListener;
	
        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener getApplicationListener () {
            if(appListener == null)appListener = new GdxGame();
            return appListener;
        	
        }

		@Override
		public ApplicationListener createApplicationListener() {
			return getApplicationListener();
		}
}