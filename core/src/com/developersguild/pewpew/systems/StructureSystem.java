package com.developersguild.pewpew.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.developersguild.pewpew.components.BodyComponent;
import com.developersguild.pewpew.components.StateComponent;
import com.developersguild.pewpew.components.StructureComponent;
import com.developersguild.pewpew.components.TransformComponent;

/**
 * Created by Vihan on 1/11/2016.
 */
public class StructureSystem extends IteratingSystem {
    private static final Family family = Family.all(StructureComponent.class,
            StateComponent.class,
            TransformComponent.class,
            BodyComponent.class).get();

    private Engine engine;

    private ComponentMapper<TransformComponent> tm;
    private ComponentMapper<StructureComponent> rm;
    private ComponentMapper<StateComponent> sm;
    private ComponentMapper<BodyComponent> bm;

    public StructureSystem() {
        super(Family.all(StructureComponent.class).get());

        tm = ComponentMapper.getFor(TransformComponent.class);
        rm = ComponentMapper.getFor(StructureComponent.class);
        sm = ComponentMapper.getFor(StateComponent.class);
        bm = ComponentMapper.getFor(BodyComponent.class);
    }

    @Override
    public void addedToEngine(Engine engine) {
        super.addedToEngine(engine);
        this.engine = engine;
    }

    @Override
    public void processEntity(Entity entity, float deltaTime) {
        StateComponent state = sm.get(entity);
        TransformComponent t = tm.get(entity);

        if (state.get() == StructureComponent.STATE_DEAD) {
            engine.removeEntity(entity);
        }
    }

    public void die(Entity entity) {
        if (family.matches(entity)) {
            StateComponent state = sm.get(entity);
            state.set(StructureComponent.STATE_DEAD);
        }
    }
}
