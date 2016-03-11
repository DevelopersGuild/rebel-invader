package io.developersguild.rebelinvader.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.developersguild.rebelinvader.Assets;
import io.developersguild.rebelinvader.Level;
import io.developersguild.rebelinvader.components.BackgroundComponent;
import io.developersguild.rebelinvader.components.TextureComponent;
import io.developersguild.rebelinvader.components.TransformComponent;

/**
 * Created by Vihan on 1/10/2016.
 */
public class BackgroundSystem extends IteratingSystem {

    float nebulaStep;
    float starsStep;
    private OrthographicCamera camera;

    private ComponentMapper<BackgroundComponent> bm;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<TextureComponent> txm;

    public BackgroundSystem() {
        super(Family.all(BackgroundComponent.class).get());

        bm = ComponentMapper.getFor(BackgroundComponent.class);
        txm = ComponentMapper.getFor(TextureComponent.class);
        tm = ComponentMapper.getFor(TransformComponent.class);

        nebulaStep = 0f;
        starsStep = 0f;
    }

    public void setCamera(OrthographicCamera camera) {
        this.camera = camera;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        BackgroundComponent bg = bm.get(entity);
        TextureComponent tx = txm.get(entity);
        TransformComponent t = tm.get(entity);

        if (bg.type == BackgroundComponent.TYPE_NEBULA) {
            nebulaStep += deltaTime * BackgroundComponent.NEBULA_VELOCITY_MULTIPLIER / 10f;
            tx.region = new TextureRegion(Assets.bgNebula, 0, -nebulaStep, Level.LEVEL_WIDTH / 2, Level.SCREEN_HEIGHT);
        } else if (bg.type == BackgroundComponent.TYPE_STARS) {
            starsStep += deltaTime * BackgroundComponent.STARS_VELOCITY_MULTIPLIER / 10f;
            tx.region = new TextureRegion(Assets.bgStars, 0, -starsStep, Level.LEVEL_WIDTH / 2, Level.SCREEN_HEIGHT);
        }

        t.pos.set(camera.position.x, camera.position.y, 10.0f);
    }
}
