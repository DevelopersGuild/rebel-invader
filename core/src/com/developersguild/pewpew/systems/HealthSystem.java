package com.developersguild.pewpew.systems;
import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.developersguild.pewpew.components.HealthComponent;
import com.developersguild.pewpew.components.TransformComponent;
/**
 * Created by Thomas on 1/23/2016.
 */
public class HealthSystem extends IteratingSystem {
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<HealthComponent> hm;
    public HealthSystem()
    {
        super(Family.all(HealthComponent.class).get());
        tm = ComponentMapper.getFor(TransformComponent.class);
        hm = ComponentMapper.getFor(HealthComponent.class);
    }
    @Override
    public void processEntity(Entity entity, float deltaTime)
    {
        TransformComponent pos = tm.get(entity);
        HealthComponent health = hm.get(entity);
    }
}
