package io.developersguild.rebelinvader.components;

import com.badlogic.ashley.core.Component;

/**
 * Created by Vihan on 1/10/2016.
 */
public class StateComponent implements Component {
    public float time = 0.0f;
    private int state = 0;

    public int get() {
        return state;
    }

    public void set(int newState) {
        state = newState;
        time = 0.0f;
    }
}
