package io.developersguild.rebelinvader.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.developersguild.rebelinvader.Level;
import io.developersguild.rebelinvader.components.BodyComponent;
import io.developersguild.rebelinvader.components.BulletComponent;
import io.developersguild.rebelinvader.components.EnemyComponent;
import io.developersguild.rebelinvader.components.ExplosionComponent;
import io.developersguild.rebelinvader.components.MissileComponent;
import io.developersguild.rebelinvader.components.MovementComponent;
import io.developersguild.rebelinvader.components.StateComponent;
import io.developersguild.rebelinvader.components.TransformComponent;

/**
 * Created by Vihan on 2/6/2016.
 */
public class EnemySystem extends IteratingSystem {
    private static final Family family = Family.all(EnemyComponent.class,
            TransformComponent.class,
            MovementComponent.class,
            BodyComponent.class,
            StateComponent.class).get();

    private float currentTime;
    private Level level;

    private ComponentMapper<EnemyComponent> em;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<MovementComponent> mm;
    private ComponentMapper<BodyComponent> bm;
    private ComponentMapper<StateComponent> sm;

    public EnemySystem(Level level) {
        super(family);
        currentTime = 0f;
        this.level = level;

        em = ComponentMapper.getFor(EnemyComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
        bm = ComponentMapper.getFor(BodyComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        EnemyComponent enemy = em.get(entity);
        TransformComponent t = tm.get(entity);
        MovementComponent mov = mm.get(entity);
        BodyComponent body = bm.get(entity);
        StateComponent state = sm.get(entity);

        currentTime += deltaTime;

        int collisionCode = 0;
        if (body.body.getUserData() != null && body.body.getUserData().getClass() == Integer.class) {
            collisionCode = (Integer) body.body.getUserData();
        }

        body.body.setUserData(this);

        // Movement handling
        // Start moving down
        mov.velocity.y = -EnemyComponent.VELOCITY_Y * deltaTime;
        if (t.pos.y - enemy.target.getComponent(TransformComponent.class).pos.y < EnemyComponent.TARGETING_RADIUS) {
            // Start X-pathfinding
            if (t.pos.x - enemy.target.getComponent(TransformComponent.class).pos.x < 0f) {
                mov.velocity.x = EnemyComponent.VELOCITY_X * deltaTime;
            } else if (t.pos.x - enemy.target.getComponent(TransformComponent.class).pos.x > 0f) {
                mov.velocity.x = -EnemyComponent.VELOCITY_X * deltaTime;
            } else {
                mov.velocity.x = 0f;
            }

            // Shooting
            if (Math.abs(t.pos.x - enemy.target.getComponent(TransformComponent.class).pos.x) <
                    EnemyComponent.WIDTH / 2f) {
                shoot(entity, enemy);
            }
        }

        // Turn off pathfinding after knockback
        if (mov.velocity.x != 0f &&
                (t.pos.y - enemy.target.getComponent(TransformComponent.class).pos.y - EnemyComponent.HEIGHT > EnemyComponent.TARGETING_RADIUS)) {
            mov.velocity.x = 0f;
        }

        // Bounds checking
        // Removes the enemy from the game if it goes off the left, right, or bottom of the screen
        if (t.pos.x + EnemyComponent.WIDTH / 2 < 0) {
            state.set(EnemyComponent.STATE_DEAD);
        }
        if (t.pos.x - EnemyComponent.WIDTH / 2 > Level.LEVEL_WIDTH) {
            state.set(EnemyComponent.STATE_DEAD);
        }
        if (t.pos.y < enemy.target.getComponent(TransformComponent.class).pos.y) {
            state.set(EnemyComponent.STATE_DEAD);
        }

        // Collision handling
        if (collisionCode == BodyComponent.BULLET_ENEMY_COLLISION) {
            enemy.currentHealth -= BulletComponent.BULLET_DAMAGE;
        }

        if (collisionCode == BodyComponent.MISSILE_ENEMY_COLLISION) {
            enemy.currentHealth -= MissileComponent.MISSILE_DAMAGE;
        }

        if (collisionCode == BodyComponent.EXPLOSION_ENEMY_COLLISION) {
            enemy.currentHealth -= ExplosionComponent.EXPLOSION_DAMAGE;
        }

        // Death
        if (enemy.currentHealth <= 0f) {
            state.set(EnemyComponent.STATE_DEAD);
            level.score += EnemyComponent.SCORE_VALUE;
        }

        checkHealthBounds(enemy);
    }

    private void checkHealthBounds(EnemyComponent enemy) {
        // Prevent health decreasing below 0
        if (enemy.currentHealth < 0) {
            enemy.currentHealth = 0;
        }

        // Prevent health increasing over maxHealth
        if (enemy.currentHealth > enemy.maxHealth) {
            enemy.currentHealth = enemy.maxHealth;
        }
    }

    private void shoot(Entity entity, EnemyComponent enemy) {
        if (enemy.shootTimer <= currentTime) {
            enemy.shootTimer = currentTime + EnemyComponent.FIRE_RATE;
            level.createBullet(entity);
        }
    }
}
