package com.developersguild.pewpew.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.developersguild.pewpew.components.BoundsComponent;
import com.developersguild.pewpew.components.EnemyComponent;
import com.developersguild.pewpew.components.HealthComponent;
import com.developersguild.pewpew.components.PlayerComponent;
import com.developersguild.pewpew.components.StructureComponent;
import com.developersguild.pewpew.components.TransformComponent;
import com.developersguild.pewpew.screens.GameScreen;

import sun.font.GlyphLayout;

/**
 * Created by Thomas on 1/23/2016.
 */
public class HealthSystem extends IteratingSystem {
    private static final Family family = Family.all(TransformComponent.class,
            HealthComponent.class).get();
    private final GameScreen screen;
    //private Engine engine;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<HealthComponent> hm;
    private float healthLastFrame;

    public HealthSystem(GameScreen screen) {
        super(family);
        this.screen = screen;

        tm = ComponentMapper.getFor(TransformComponent.class);
        hm = ComponentMapper.getFor(HealthComponent.class);

        healthLastFrame = 0f;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        //this.engine = engine;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent pos = tm.get(entity);
        HealthComponent health = hm.get(entity);

        // Health bar follows target
        //pos.pos.x = health.targetPos.x - ((1 - pos.scale.x) * health.target.getComponent(BoundsComponent.class).bounds.width / 2); -- PINS BAR TO THE LEFT EDGE
        pos.pos.x = health.targetPos.x;
        pos.pos.y = health.targetPos.y - health.target.getComponent(BoundsComponent.class).bounds.height / 2f - 0.1f;

        if (health.target.getComponent(PlayerComponent.class) != null) {
            health.currentHealth = health.target.getComponent(PlayerComponent.class).currentHealth;
            if (health.currentHealth != healthLastFrame) updateHealthBar(entity);
        } else if (health.target.getComponent(StructureComponent.class) != null) {
            health.currentHealth = health.target.getComponent(StructureComponent.class).currentHealth;
            if (health.currentHealth != healthLastFrame) updateHealthBar(entity);
        } else if (health.target.getComponent(EnemyComponent.class) != null) {
            health.currentHealth = health.target.getComponent(EnemyComponent.class).currentHealth;
            if (health.currentHealth != healthLastFrame) updateHealthBar(entity);
        }

        healthLastFrame = health.currentHealth;

        if (health.currentHealth <= 0) {
            screen.markEntityForRemoval(health.target);
            screen.markEntityForRemoval(entity);
        }
    }

    public void updateHealthBar(Entity entity) {
        HealthComponent health = hm.get(entity);
        TransformComponent pos = tm.get(entity);

        // Update healthLength
        float healthLength = health.currentHealth / health.maxHealth * health.lengthRatio;

        checkHealthBounds(health);

        pos.scale.set(healthLength, health.widthRatio);
    }

    private void checkHealthBounds(HealthComponent health) {
        // Prevent health decreasing below 0
        if (health.currentHealth < 0) {
            health.currentHealth = 0;
        }

        // Prevent health increasing over maxHealth
        if (health.currentHealth > health.maxHealth) {
            health.currentHealth = health.maxHealth;
        }

        // Do the same for the actual target entity too
        if (health.target.getComponent(PlayerComponent.class) != null) {
            health.target.getComponent(PlayerComponent.class).currentHealth = health.currentHealth;
        } else if (health.target.getComponent(PlayerComponent.class) != null) {
            health.target.getComponent(PlayerComponent.class).currentHealth = health.currentHealth;
        } else if (health.target.getComponent(PlayerComponent.class) != null) {
            health.target.getComponent(PlayerComponent.class).currentHealth = health.currentHealth;
        }
    }

    public void nextLevel(Entity entity) {
        HealthComponent.healthMultiplier += HealthComponent.LEVEL_INC;
        HealthComponent.currentLvl++;
    }
}
