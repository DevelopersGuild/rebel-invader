package com.developersguild.pewpew.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.developersguild.pewpew.components.BodyComponent;
import com.developersguild.pewpew.components.BulletComponent;
import com.developersguild.pewpew.components.MovementComponent;
import com.developersguild.pewpew.components.PlayerComponent;
import com.developersguild.pewpew.components.StateComponent;
import com.developersguild.pewpew.components.TransformComponent;
import com.developersguild.pewpew.screens.GameScreen;

/**
 * Created by Vihan on 2/9/2016.
 */
public class BulletSystem extends IteratingSystem {
    private static final Family family = Family.all(BodyComponent.class,
            BulletComponent.class,
            MovementComponent.class,
            StateComponent.class,
            TransformComponent.class).get();
    private final GameScreen screen;
    private Engine engine;
    private ComponentMapper<BodyComponent> bm;
    private ComponentMapper<BulletComponent> blm;
    private ComponentMapper<MovementComponent> mm;
    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TransformComponent> tm;

    public BulletSystem(GameScreen screen) {
        super(Family.all(BulletComponent.class).get());
        this.screen = screen;

        bm = ComponentMapper.getFor(BodyComponent.class);
        blm = ComponentMapper.getFor(BulletComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BodyComponent body = bm.get(entity);
        BulletComponent bullet = blm.get(entity);
        MovementComponent mov = mm.get(entity);
        StateComponent state = sm.get(entity);
        TransformComponent pos = tm.get(entity);

        int collisionCode = 0;
        if (body.body.getUserData() != null && body.body.getUserData().getClass() == Integer.class) {
            collisionCode = (Integer) body.body.getUserData();
        }

        body.body.setUserData(entity);

        if (bullet.origin.getComponent(PlayerComponent.class) != null) { // If player fired
            mov.velocity.y = BulletComponent.VELOCITY * deltaTime;
        } else { // If anyone else fired
            mov.velocity.y = -BulletComponent.VELOCITY * deltaTime;
        }

        if (collisionCode == BodyComponent.BULLET_ENEMY_COLLISION ||
                collisionCode == BodyComponent.BULLET_STRUCTURE_COLLISION ||
                collisionCode == BodyComponent.PLAYER_BULLET_COLLISION) {
            screen.markEntityForRemoval(entity);
        }
    }
}
