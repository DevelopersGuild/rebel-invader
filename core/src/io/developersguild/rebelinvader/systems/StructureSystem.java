package io.developersguild.rebelinvader.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.developersguild.rebelinvader.Level;
import io.developersguild.rebelinvader.components.BodyComponent;
import io.developersguild.rebelinvader.components.BulletComponent;
import io.developersguild.rebelinvader.components.EnemyComponent;
import io.developersguild.rebelinvader.components.ExplosionComponent;
import io.developersguild.rebelinvader.components.MissileComponent;
import io.developersguild.rebelinvader.components.PlayerComponent;
import io.developersguild.rebelinvader.components.StateComponent;
import io.developersguild.rebelinvader.components.StructureComponent;
import io.developersguild.rebelinvader.components.TransformComponent;

/**
 * Created by Vihan on 1/11/2016.
 */
public class StructureSystem extends IteratingSystem {
    private static final Family family = Family.all(StructureComponent.class,
            TransformComponent.class,
            BodyComponent.class).get();

    private ComponentMapper<StructureComponent> sm;
    private ComponentMapper<BodyComponent> bm;
    private ComponentMapper<StateComponent> stm;

    private Level level;

    public StructureSystem(Level level) {
        super(family);
        this.level = level;

        ComponentMapper.getFor(TransformComponent.class);
        sm = ComponentMapper.getFor(StructureComponent.class);
        bm = ComponentMapper.getFor(BodyComponent.class);
        stm = ComponentMapper.getFor(StateComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        //TransformComponent t = tm.get(entity);
        BodyComponent body = bm.get(entity);
        StructureComponent structure = sm.get(entity);
        StateComponent state = stm.get(entity);

        int collisionCode = 0;
        if (body.body.getUserData() != null && body.body.getUserData().getClass() == Integer.class) {
            collisionCode = (Integer) body.body.getUserData();
        }

        body.body.setUserData(this);

        // Collision handling
        if (collisionCode == BodyComponent.BULLET_STRUCTURE_COLLISION) {
            structure.currentHealth -= BulletComponent.BULLET_DAMAGE;
        }

        if (collisionCode == BodyComponent.MISSILE_STRUCTURE_COLLISION) {
            structure.currentHealth -= MissileComponent.MISSILE_DAMAGE;
        }

        if (collisionCode == BodyComponent.EXPLOSION_STRUCTURE_COLLISION) {
            structure.currentHealth -= ExplosionComponent.EXPLOSION_DAMAGE;
        }

        // Death
        if (structure.currentHealth <= 0f) {
            state.set(EnemyComponent.STATE_DEAD);

            level.score += StructureComponent.SCORE_VALUE;
        }

        checkHealthBounds(structure);
    }

    private void checkHealthBounds(StructureComponent structure) {
        // Prevent health decreasing below 0
        if (structure.currentHealth < 0) {
            structure.currentHealth = 0;
        }

        // Prevent health increasing over maxHealth
        if (structure.currentHealth > structure.maxHealth) {
            structure.currentHealth = structure.maxHealth;
        }
    }
}
