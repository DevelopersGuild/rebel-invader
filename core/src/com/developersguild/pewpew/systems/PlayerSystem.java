package com.developersguild.pewpew.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.developersguild.pewpew.Level;
import com.developersguild.pewpew.screens.GameScreen;
import com.developersguild.pewpew.components.BodyComponent;
import com.developersguild.pewpew.components.MovementComponent;
import com.developersguild.pewpew.components.PlayerComponent;
import com.developersguild.pewpew.components.StateComponent;
import com.developersguild.pewpew.components.HealthComponent;
import com.developersguild.pewpew.components.StructureComponent;
import com.developersguild.pewpew.components.TransformComponent;

/**
 * Created by Vihan on 1/10/2016.
 */
public class PlayerSystem extends IteratingSystem {
    private static final Family family = Family.all(PlayerComponent.class,
            StateComponent.class,
            TransformComponent.class,
            MovementComponent.class,
            BodyComponent.class,
            HealthComponent.class).get();

    private float accelX = 0.0f;

    private ComponentMapper<PlayerComponent> rm;
    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<MovementComponent> mm;
    private ComponentMapper<BodyComponent> bm;
    private ComponentMapper<HealthComponent> hm;

    public PlayerSystem(Level level) {
        super(family);

        rm = ComponentMapper.getFor(PlayerComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
        bm = ComponentMapper.getFor(BodyComponent.class);
        hm = ComponentMapper.getFor(HealthComponent.class);
    }

    public void setAccelX(float accelX) {
        this.accelX = accelX;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        accelX = 0.0f;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        TransformComponent t = tm.get(entity);
        StateComponent state = sm.get(entity);
        MovementComponent mov = mm.get(entity);
        PlayerComponent player = rm.get(entity);
        BodyComponent body = bm.get(entity);
        HealthComponent health = hm.get(entity);

        int collisionCode = 0;
        if (body.body.getUserData() != null && body.body.getUserData().getClass() == Integer.class) {
            collisionCode = (Integer) body.body.getUserData();
        }

        body.body.setUserData(this);

        // Movement handling
        if (state.get() == PlayerComponent.STATE_NORMAL) {
            mov.velocity.x = -accelX / 10.0f * PlayerComponent.MOVE_VELOCITY_X * deltaTime;
            mov.velocity.y = PlayerComponent.MOVE_VELOCITY_Y * deltaTime;
        }

        // Bounds checking
        if (t.pos.x - player.WIDTH / 2 < 0) {
            t.pos.x = player.WIDTH / 2;
        }

        if (t.pos.x + player.WIDTH / 2 > Level.WORLD_WIDTH) {
            t.pos.x = Level.WORLD_WIDTH - player.WIDTH / 2;
        }

        // Collision handling
        if (collisionCode == BodyComponent.PLAYER_STRUCTURE_COLLISION) {
            // TODO: Give player ~1 second of invulnerability, and make the sprite blink for this duration
            player.currentHealth -= StructureComponent.DAMAGE;
        }

        // Tilting
        if (mov.velocity.x >= 2f) {
            t.rotation = -0.1f;
        }

        if (mov.velocity.x <= -2f) {
            t.rotation = 0.1f;
        }

        if (mov.velocity.x < 1f && mov.velocity.x > -1f) {
            t.rotation = 0.0f;
        }

        player.heightSoFar = t.pos.y;

        if (health.currentHealth == 0) {
            die();
        }
    }


    public void die() {
        //need to make the CASE in GameScreen GameOver.
    }



}
