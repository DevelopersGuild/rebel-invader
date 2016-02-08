package com.developersguild.pewpew.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.developersguild.pewpew.components.BodyComponent;
import com.developersguild.pewpew.components.HeightDisposableComponent;
import com.developersguild.pewpew.components.StructureComponent;
import com.developersguild.pewpew.components.TransformComponent;

public class HeightDisposableSystem extends IteratingSystem {
	
    private static final Family family = Family.all(
    		HeightDisposableComponent.class,
            TransformComponent.class).get();

    private Engine engine;

    private ComponentMapper<HeightDisposableComponent> hm;
    private ComponentMapper<TransformComponent> tm;

    public HeightDisposableSystem() {
        super(Family.all(StructureComponent.class).get());

        tm = ComponentMapper.getFor(TransformComponent.class);
        hm = ComponentMapper.getFor(HeightDisposableComponent.class);
    }

	@Override
	protected void processEntity(Entity entity, float partialTime) {
		
	}

}
