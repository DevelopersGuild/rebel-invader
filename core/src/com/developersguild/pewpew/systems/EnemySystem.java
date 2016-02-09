package com.developersguild.pewpew.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
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
            BodyComponent.class,
            StateComponent.class).get();

    private Engine engine;

    private ComponentMapper<EnemyComponent> em;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<MovementComponent> mm;
    private ComponentMapper<BodyComponent> bm;
    private ComponentMapper<StateComponent> sm;

    public EnemySystem() {
        super(family);

        em = ComponentMapper.getFor(EnemyComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
        bm = ComponentMapper.getFor(BodyComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
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
        StateComponent state = sm.get(entity);

        body.body.setUserData(this);

        // Movement handling
        // Start moving down if on screen
        if (t.pos.y - enemy.target.getComponent(TransformComponent.class).pos.y - EnemyComponent.HEIGHT < 12.5f) {
            mov.velocity.y = -EnemyComponent.VELOCITY_Y * deltaTime;
        }
        // X-pathfinding


        // Bounds checking
        // Removes the enemy from the game if it goes off the left, right, or bottom of the screen
        if (t.pos.x + enemy.WIDTH / 2 < 0) {
            state.set(EnemyComponent.STATE_DEAD);
        }
        if (t.pos.x - enemy.WIDTH / 2 > Level.LEVEL_WIDTH) {
            state.set(EnemyComponent.STATE_DEAD);
        }
        if (t.pos.y - enemy.target.getComponent(TransformComponent.class).pos.y - EnemyComponent.HEIGHT / 2 < -2.5f) {
            state.set(EnemyComponent.STATE_DEAD);
        }

        // Death
        if (enemy.currentHealth <= 0f) {
            state.set(EnemyComponent.STATE_DEAD);
        }

        if (state.get() == EnemyComponent.STATE_DEAD) {
            //engine.removeEntity(entity);
        }
    }
}
