package com.developersguild.pewpew.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.developersguild.pewpew.components.BodyComponent;
import com.developersguild.pewpew.components.StructureComponent;
import com.developersguild.pewpew.components.TransformComponent;

/**
 * Created by Vihan on 1/11/2016.
 */
public class StructureSystem extends IteratingSystem {
    private static final Family family = Family.all(StructureComponent.class,
            TransformComponent.class,
            BodyComponent.class).get();

    private Engine engine;

    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<StructureComponent> sm;
    private ComponentMapper<BodyComponent> bm;

    public StructureSystem() {
        super(Family.all(StructureComponent.class).get());

        tm = ComponentMapper.getFor(TransformComponent.class);
        sm = ComponentMapper.getFor(StructureComponent.class);
        bm = ComponentMapper.getFor(BodyComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TransformComponent t = tm.get(entity);
        BodyComponent body = bm.get(entity);
        StructureComponent structure = sm.get(entity);

        body.body.setUserData(this);

        // Death
        if (structure.currentHealth <= 0f) {
            engine.removeEntity(entity);
        }
    }
}
