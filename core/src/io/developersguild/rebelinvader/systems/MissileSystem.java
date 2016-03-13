package io.developersguild.rebelinvader.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.developersguild.rebelinvader.Level;
import io.developersguild.rebelinvader.components.BodyComponent;
import io.developersguild.rebelinvader.components.MissileComponent;
import io.developersguild.rebelinvader.components.MovementComponent;
import io.developersguild.rebelinvader.components.StateComponent;
import io.developersguild.rebelinvader.components.TransformComponent;
import io.developersguild.rebelinvader.screens.GameScreen;

/**
 * Created by thomastran on 3/4/2016.
 */
public class MissileSystem extends IteratingSystem {
    private static final Family family = Family.all(BodyComponent.class,
            MissileComponent.class,
            MovementComponent.class,
            StateComponent.class,
            TransformComponent.class).get();
    private final GameScreen screen;
    private ComponentMapper<BodyComponent> bm;
    private ComponentMapper<MissileComponent> mim;
    private ComponentMapper<MovementComponent> mm;
    private Level level;

    private boolean shouldDetonate;

    public MissileSystem(GameScreen screen, Level level) {
        super(family);
        this.screen = screen;
        this.level = level;

        bm = ComponentMapper.getFor(BodyComponent.class);
        mim = ComponentMapper.getFor(MissileComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
        //sm = ComponentMapper.getFor(StateComponent.class);
        //tm = ComponentMapper.getFor(TransformComponent.class);
    }

    public void detonateMissile() {
        this.shouldDetonate = true;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BodyComponent body = bm.get(entity);
        MissileComponent missile = mim.get(entity);
        MovementComponent mov = mm.get(entity);

        mov.velocity.x = 0;
        //StateComponent state = sm.get(entity);
        //TransformComponent pos = tm.get(entity);

        int collisionCode = 0;
        if (body.body.getUserData() != null && body.body.getUserData().getClass() == Integer.class) {
            collisionCode = (Integer) body.body.getUserData();
        }

        body.body.setUserData(entity);

        // Velocity increases over time: doubles, triples, quadruples, etc
        mov.velocity.y = (MissileComponent.PLAYER_MISSILE_VELOCITY + missile.accelerator) * deltaTime;
        missile.accelerator += deltaTime * MissileComponent.PLAYER_MISSILE_VELOCITY;

        if (collisionCode == BodyComponent.BULLET_ENEMY_COLLISION ||
                collisionCode == BodyComponent.BULLET_STRUCTURE_COLLISION ||
                collisionCode == BodyComponent.PLAYER_BULLET_COLLISION) {
            screen.markEntityForRemoval(entity);
        }

        if (this.shouldDetonate) {
            screen.markEntityForRemoval(entity);
            this.shouldDetonate = false;
            //level.createExplosion(entity);
        }
    }
}
