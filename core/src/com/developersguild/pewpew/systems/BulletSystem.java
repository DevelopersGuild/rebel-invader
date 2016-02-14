package com.developersguild.pewpew.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.developersguild.pewpew.Level;
import com.developersguild.pewpew.components.BodyComponent;
import com.developersguild.pewpew.components.BulletComponent;
import com.developersguild.pewpew.components.MovementComponent;
import com.developersguild.pewpew.components.PlayerComponent;
import com.developersguild.pewpew.components.StateComponent;
import com.developersguild.pewpew.components.TransformComponent;

/**
 * Created by Vihan on 2/9/2016.
 */
public class BulletSystem extends IteratingSystem {
    private static final Family family = Family.all(BodyComponent.class,
            BulletComponent.class,
            MovementComponent.class,
            StateComponent.class,
            TransformComponent.class).get();

    private ComponentMapper<BodyComponent> bm;
    private ComponentMapper<BulletComponent> blm;
    private ComponentMapper<MovementComponent> mm;
    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<TransformComponent> tm;

    public BulletSystem() {
        super(Family.all(BulletComponent.class).get());

        bm = ComponentMapper.getFor(BodyComponent.class);
        blm = ComponentMapper.getFor(BulletComponent.class);
        mm = ComponentMapper.getFor(MovementComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);
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

        body.body.setUserData(this);

        Gdx.app.log(getClass().getSimpleName(), "STUFF");

        mov.velocity.y = BulletComponent.VELOCITY * deltaTime;
    }
}
