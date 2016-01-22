package com.developersguild.pewpew.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.developersguild.pewpew.components.BodyComponent;
import com.developersguild.pewpew.components.TransformComponent;

/**
 * Created by Vihan on 1/22/2016.
 */
public class PhysicsSystem extends IteratingSystem {

    private static final float MAX_STEP_TIME = 1/45f;
    private static float accumulator = 0f;

    private World world;
    private Array<Entity> bodiesQueue;

    // TODO: Remove all instances of debugRenderer on release
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;

    private ComponentMapper<BodyComponent> bm = ComponentMapper.getFor(BodyComponent.class);
    private ComponentMapper<TransformComponent> tm = ComponentMapper.getFor(TransformComponent.class);

    public PhysicsSystem(World world, OrthographicCamera camera) {
        super(Family.all(BodyComponent.class, TransformComponent.class).get());

        debugRenderer = new Box2DDebugRenderer();
        this.world = world;
        this.camera = camera;
        this.bodiesQueue = new Array<Entity>();
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        debugRenderer.render(world, camera.combined);
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        if (accumulator >= MAX_STEP_TIME) {
            world.step(MAX_STEP_TIME, 6, 2);
            accumulator -= MAX_STEP_TIME;

            // Entity Queue
            for (Entity entity: bodiesQueue) {
                TransformComponent t = tm.get(entity);
                BodyComponent body = bm.get(entity);
                Vector2 pos = body.body.getPosition();
                pos.x = t.pos.x;
                pos.y = t.pos.y;
                body.body.setTransform(pos, t.rotation * MathUtils.degreesToRadians);
            }
        }
        bodiesQueue.clear();
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        bodiesQueue.add(entity);
    }
}
