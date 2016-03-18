package io.developersguild.rebelinvader.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;

import io.developersguild.rebelinvader.Assets;
import io.developersguild.rebelinvader.components.BoundsComponent;
import io.developersguild.rebelinvader.components.PlayerComponent;
import io.developersguild.rebelinvader.components.PowerComponent;
import io.developersguild.rebelinvader.components.TransformComponent;
import io.developersguild.rebelinvader.screens.GameScreen;

/**
 * Created by MW on 04-Mar-16.
 */
public class PowerSystem extends IteratingSystem {
    private static final Family family = Family.all(TransformComponent.class,
            PowerComponent.class).get();
    private final GameScreen screen;
    //private Engine engine;
    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<PowerComponent> pm;
    private float powerLastFrame;
    private boolean isPowerup;

    public PowerSystem(GameScreen screen) {
        super(family);
        this.screen = screen;

        tm = ComponentMapper.getFor(TransformComponent.class);
        pm = ComponentMapper.getFor(PowerComponent.class);

        powerLastFrame = 0f;
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent pos = tm.get(entity);
        PowerComponent power = pm.get(entity);

        // Power bar follows target
        pos.pos.x = power.targetPos.x;
        pos.pos.y = power.targetPos.y - power.target.getComponent(BoundsComponent.class).bounds.height / 2f - 0.2f;

        if (power.target.getComponent(PlayerComponent.class) != null) {
            power.currentPower = power.target.getComponent(PlayerComponent.class).currentPower;
            if (power.currentPower != powerLastFrame) updatePowerBar(entity);
        }

        // Prevent power bar being rendered when it's empty
        if (power.currentPower <= 0.1) power.doRender = false;
        else power.doRender = true;

        powerLastFrame = power.currentPower;
    }

    public void updatePowerBar(Entity entity) {
        PowerComponent power = pm.get(entity);
        TransformComponent pos = tm.get(entity);

        // Update powerLength
        float powerLength = power.currentPower / power.maxPower * power.lengthRatio;

        checkPowerBounds(power);

        pos.scale.set(powerLength, power.widthRatio);
    }

    private void checkPowerBounds(PowerComponent power) {
        // Prevent power decreasing below 0
        if (power.currentPower <= 0) {
            power.currentPower = 0;
        }

        // Prevent health increasing over maxHealth
        if (power.currentPower >= power.maxPower) {
            power.currentPower = power.maxPower;
            screen.activatePower();
            Assets.powerup.play(0.4f);
        }

        // Do the same for the actual target entity too
        if (power.target.getComponent(PlayerComponent.class) != null) {
            power.target.getComponent(PlayerComponent.class).currentPower = power.currentPower;
        }
    }
}
