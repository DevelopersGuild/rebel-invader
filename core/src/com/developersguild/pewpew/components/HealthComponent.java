package com.developersguild.pewpew.components;
import com.badlogic.ashley.core.Component;
/**
 * Created by Thomas on 1/22/2016.
 */
public class HealthComponent implements Component {
    public final int MAX_HEALTH = 100;
    public int currentHealth;
}
