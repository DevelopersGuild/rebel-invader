package com.developersguild.pewpew.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.developersguild.pewpew.components.BodyComponent;
import com.developersguild.pewpew.components.MissileComponent;
import com.developersguild.pewpew.components.MovementComponent;
import com.developersguild.pewpew.components.PlayerComponent;
import com.developersguild.pewpew.components.StateComponent;
import com.developersguild.pewpew.components.TransformComponent;
import com.developersguild.pewpew.screens.GameScreen;

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
    private ComponentMapper<MissileComponent> blm;
    private ComponentMapper<MovementComponent> mm;
    //private ComponentMapper<StateComponent> sm;
    //private ComponentMapper<TransformComponent> tm;

    public MissileSystem(GameScreen screen) {
        super(family);
        this.screen = screen;

        bm = ComponentMapper.getFor(BodyComponent.class);
        blm = ComponentMapper.getFor(MissileComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
        //sm = ComponentMapper.getFor(StateComponent.class);
        //tm = ComponentMapper.getFor(TransformComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BodyComponent body = bm.get(entity);
        MissileComponent missile = blm.get(entity);
        MovementComponent mov = mm.get(entity);
        //StateComponent state = sm.get(entity);
        //TransformComponent pos = tm.get(entity);

        int collisionCode = 0;
        if (body.body.getUserData() != null && body.body.getUserData().getClass() == Integer.class) {
            collisionCode = (Integer) body.body.getUserData();
        }

        body.body.setUserData(entity);


        mov.velocity.y = MissileComponent.PLAYER_MISSILE_VELOCITY * deltaTime;
        System.out.println(mov.velocity.y);


        if (collisionCode == BodyComponent.BULLET_ENEMY_COLLISION ||
                collisionCode == BodyComponent.BULLET_STRUCTURE_COLLISION ||
                collisionCode == BodyComponent.PLAYER_BULLET_COLLISION) {
            screen.markEntityForRemoval(entity);
        }
    }
}
