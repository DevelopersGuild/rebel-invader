package com.developersguild.pewpew.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.developersguild.pewpew.Assets;
import com.developersguild.pewpew.Level;
import com.developersguild.pewpew.components.BackgroundComponent;
import com.developersguild.pewpew.components.TextureComponent;
import com.developersguild.pewpew.components.TransformComponent;

/**
 * Created by Vihan on 1/10/2016.
 */
public class BackgroundSystem extends IteratingSystem {
    private OrthographicCamera camera;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<TextureComponent> txm;

    float bgStep;

    public BackgroundSystem() {
        super(Family.all(BackgroundComponent.class).get());
        txm = ComponentMapper.getFor(TextureComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);

        bgStep = 0f;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);

        bgStep += deltaTime / 10f;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TextureComponent tx = txm.get(entity);
        TransformComponent t = tm.get(entity);

        tx.region = new TextureRegion(Assets.background, 0, -bgStep, Level.LEVEL_WIDTH, Level.SCREEN_HEIGHT);
        t.pos.set(camera.position.x, camera.position.y, 10.0f);
    }
}
