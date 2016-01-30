package com.developersguild.pewpew.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.developersguild.pewpew.components.HealthComponent;
import com.developersguild.pewpew.components.StructureComponent;
import com.developersguild.pewpew.components.TransformComponent;

/**
 * Created by Thomas on 1/23/2016.
 */
public class HealthSystem extends IteratingSystem {
    private static final Family family = Family.all(TransformComponent.class,
            HealthComponent.class, StructureComponent.class).get();

    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<HealthComponent> hm;
    private ComponentMapper<StructureComponent> rm;

    private Engine engine;

    public HealthSystem()
    {
        super(Family.all(TransformComponent.class, HealthComponent.class).get());
        tm = ComponentMapper.getFor(TransformComponent.class);
        hm = ComponentMapper.getFor(HealthComponent.class);
        rm = ComponentMapper.getFor(StructureComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine)
    {
        super.addedToEngine(engine);
        this.engine = engine;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime)
    {
        StructureSystem structureSystem = engine.getSystem(StructureSystem.class);

        TransformComponent pos = tm.get(entity);
        HealthComponent health = hm.get(entity);

        if (health.currentHealth == 0)
        {
            // TODO: Determine entity type and call the die method for specific entity
            structureSystem.die(entity);    // For testing purposes
        }

        // Healthbar follows target
        pos.pos.x = health.target.x;
        pos.pos.y = health.target.y + 1.25f;
    }

    public void takeDamage(Entity entity, int damageValue)
    {
        if (family.matches(entity)) return;

        HealthComponent health = hm.get(entity);
        TransformComponent pos = tm.get(entity);

        health.currentHealth -= damageValue;

        // Prevent health decreasing below 0
        if (health.currentHealth < 0)
        {
            health.currentHealth = 0;
        }

        // Update healthLength
        float healthLength = (float)health.currentHealth / (float)health.maxHealth * (2.0f / 3.0f);
        pos.scale.set(healthLength, 2.0f / 3.0f);
    }

    public void recoverHealth(Entity entity, int recoverValue)
    {
        if (family.matches(entity)) return;

        HealthComponent health = hm.get(entity);
        TransformComponent pos = tm.get(entity);

        health.currentHealth += recoverValue;

        // Prevent health increasing over maxHealth
        if (health.currentHealth > health.maxHealth)
        {
            health.currentHealth = health.maxHealth;
        }

        // Update healthLength
        float healthLength = (float)health.currentHealth / (float)health.maxHealth * (2.0f / 3.0f);
        pos.scale.set(healthLength, 2.0f / 3.0f);
    }

    public void nextLevel(Entity entity)
    {
        if (family.matches(entity)) return;

        HealthComponent health = hm.get(entity);

        health.healthMultiplier += health.LEVEL_INC;
        health.currentLvl++;
    }
}
