package com.developersguild.pewpew.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.developersguild.pewpew.components.BoundsComponent;
import com.developersguild.pewpew.components.HealthComponent;
import com.developersguild.pewpew.components.TransformComponent;

/**
 * Created by Thomas on 1/23/2016.
 */
public class HealthSystem extends IteratingSystem {
    private static final Family family = Family.all(TransformComponent.class,
            HealthComponent.class).get();

    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<HealthComponent> hm;
    private Engine engine;

    public HealthSystem() {
        super(Family.all(TransformComponent.class, HealthComponent.class).get());
        tm = ComponentMapper.getFor(TransformComponent.class);
        hm = ComponentMapper.getFor(HealthComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TransformComponent pos = tm.get(entity);
        HealthComponent health = hm.get(entity);

        // Health bar follows target
        //pos.pos.x = health.targetPos.x - ((1 - pos.scale.x) * health.target.getComponent(BoundsComponent.class).bounds.width / 2); -- PINS BAR TO THE LEFT EDGE
        pos.pos.x = health.targetPos.x;
        pos.pos.y = health.targetPos.y - health.target.getComponent(BoundsComponent.class).bounds.height / 2f - 0.1f;
    }

    public void takeDamage(Entity entity, float damageValue) {
        if (!family.matches(entity)) return;

        HealthComponent health = hm.get(entity);
        TransformComponent pos = tm.get(entity);

        health.currentHealth -= damageValue;

        // Prevent health decreasing below 0
        if (health.currentHealth < 0) {
            health.currentHealth = 0;
        }

        // Update healthLength
        float healthLength = 0.0f;

        if (health.isPlayer) {
            healthLength = (float) health.currentHealth / (float) health.maxHealth * health.lengthRatio;
        }
        else if (health.isStructure) {
            healthLength = (float) health.currentHealth / (float) health.maxHealth * health.lengthRatio;
        }

        pos.scale.set(healthLength, health.widthRatio);
    }

    public void recoverHealth(Entity entity, float recoverValue) {
        if (!family.matches(entity)) return;

        HealthComponent health = hm.get(entity);
        TransformComponent pos = tm.get(entity);

        health.currentHealth += recoverValue;

        // Prevent health increasing over maxHealth
        if (health.currentHealth > health.maxHealth) {
            health.currentHealth = health.maxHealth;
        }

        // Update healthLength
        float healthLength = 0.0f;

        if (health.isPlayer) {
            healthLength = health.currentHealth / health.maxHealth * health.lengthRatio;
        }
        else if (health.isStructure) {
            healthLength = health.currentHealth / health.maxHealth * health.lengthRatio;
        }

        pos.scale.set(healthLength, health.widthRatio);
    }

    public void nextLevel(Entity entity) {
        if (!family.matches(entity)) return;

        HealthComponent health = hm.get(entity);

        health.healthMultiplier += health.LEVEL_INC;
        health.currentLvl++;
    }
}
