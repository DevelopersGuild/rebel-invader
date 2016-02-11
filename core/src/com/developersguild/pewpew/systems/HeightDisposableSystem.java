package com.developersguild.pewpew.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.developersguild.pewpew.Level;
import com.developersguild.pewpew.components.HeightDisposableComponent;
import com.developersguild.pewpew.components.StructureComponent;
import com.developersguild.pewpew.components.TransformComponent;
import com.developersguild.pewpew.screens.GameScreen;

public class HeightDisposableSystem extends IteratingSystem {

    private static final Family family = Family.all(
            HeightDisposableComponent.class,
            TransformComponent.class).get();

    private final GameScreen screen;

    private ComponentMapper<HeightDisposableComponent> hm;
    private ComponentMapper<TransformComponent> tm;

    public HeightDisposableSystem(GameScreen screen) {
        super(Family.all(StructureComponent.class).get());

        tm = ComponentMapper.getFor(TransformComponent.class);
        hm = ComponentMapper.getFor(HeightDisposableComponent.class);

        this.screen = screen;
    }

    @Override
    protected void processEntity(Entity entity, float partialTime) {
        TransformComponent t = tm.get(entity);

        if (t.pos.y < Level.playerHeight - 0.5f * Level.SCREEN_HEIGHT) {
            screen.markEntityForRemoval(entity);
        }
    }

}
