package io.developersguild.rebelinvader.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.developersguild.rebelinvader.components.BodyComponent;
import io.developersguild.rebelinvader.components.ExplosionComponent;
import io.developersguild.rebelinvader.components.TransformComponent;
import io.developersguild.rebelinvader.screens.GameScreen;

/**
 * Created by Thomas on 3/12/2016.
 */
public class ExplosionSystem extends IteratingSystem {
    private static final Family family = Family.all(ExplosionComponent.class,
            BodyComponent.class).get();

    private final GameScreen screen;

    private ComponentMapper<ExplosionComponent> em;
    private ComponentMapper<BodyComponent> bm;

    public ExplosionSystem(GameScreen screen) {
        super(family);
        this.screen = screen;

        ComponentMapper.getFor(TransformComponent.class);
        em = ComponentMapper.getFor(ExplosionComponent.class);
        bm = ComponentMapper.getFor(BodyComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        ExplosionComponent explosion = em.get(entity);
        BodyComponent body = bm.get(entity);

        body.body.setUserData(this);

        if (explosion.currentTime >= explosion.REMOVAL_TIME)
        {
            screen.markEntityForRemoval(entity);
        }
        explosion.currentTime += deltaTime;
    }
}
