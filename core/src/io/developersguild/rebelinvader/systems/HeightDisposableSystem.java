package io.developersguild.rebelinvader.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import io.developersguild.rebelinvader.Level;
import io.developersguild.rebelinvader.components.HeightDisposableComponent;
import io.developersguild.rebelinvader.components.StructureComponent;
import io.developersguild.rebelinvader.components.TransformComponent;
import io.developersguild.rebelinvader.screens.GameScreen;

public class HeightDisposableSystem extends IteratingSystem {

    /*
    private static final Family family = Family.all(
            HeightDisposableComponent.class,
            TransformComponent.class).get();
            */
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
        HeightDisposableComponent hdc = hm.get(entity);

        if (t.pos.y < Level.playerHeight - 0.5f * Level.SCREEN_HEIGHT
                || t.pos.y > Level.playerHeight + 2 * Level.SCREEN_HEIGHT) {
            screen.markEntityForRemoval(entity);
            if (hdc.childEntity != null) {
                screen.markEntityForRemoval(hdc.childEntity);
            }
        }
    }
}
