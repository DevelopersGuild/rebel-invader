package com.developersguild.pewpew.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.developersguild.pewpew.Level;
import com.developersguild.pewpew.components.BodyComponent;
import com.developersguild.pewpew.components.EnemyComponent;
import com.developersguild.pewpew.components.MovementComponent;
import com.developersguild.pewpew.components.StateComponent;
import com.developersguild.pewpew.components.TransformComponent;

/**
 * Created by Vihan on 2/6/2016.
 */
public class EnemySystem extends IteratingSystem {
    private static final Family family = Family.all(EnemyComponent.class,
            TransformComponent.class,
            MovementComponent.class,
            BodyComponent.class).get();

    private Engine engine;

    private ComponentMapper<EnemyComponent> em;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<MovementComponent> mm;
    private ComponentMapper<BodyComponent> bm;

    public EnemySystem() {
        super(family);

        em = ComponentMapper.getFor(EnemyComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
        bm = ComponentMapper.getFor(BodyComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EnemyComponent enemy = em.get(entity);
        TransformComponent t = tm.get(entity);
        MovementComponent mov = mm.get(entity);
        BodyComponent body = bm.get(entity);

        body.body.setUserData(this);

        // Movement handling

        // Bounds checking
        // Removes the enemy from the game if it goes off the left or right side of the screen
        if (t.pos.x + enemy.WIDTH / 2 < 0) {
            engine.removeEntity(entity);
        }

        if (t.pos.x - enemy.WIDTH / 2 > Level.LEVEL_WIDTH) {
            engine.removeEntity(entity);
        }

        // Death
        if (enemy.currentHealth <= 0f) {
            engine.removeEntity(entity);
        }
    }
}
