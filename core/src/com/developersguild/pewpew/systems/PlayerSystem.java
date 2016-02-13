package com.developersguild.pewpew.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.developersguild.pewpew.Level;
import com.developersguild.pewpew.components.BodyComponent;
import com.developersguild.pewpew.components.EnemyComponent;
import com.developersguild.pewpew.components.MovementComponent;
import com.developersguild.pewpew.components.PlayerComponent;
import com.developersguild.pewpew.components.StateComponent;
import com.developersguild.pewpew.components.StructureComponent;
import com.developersguild.pewpew.components.TransformComponent;

/**
 * Created by Vihan on 1/10/2016.
 */
public class PlayerSystem extends IteratingSystem {
	private static final Family family = Family.all(PlayerComponent.class,
			StateComponent.class,
			TransformComponent.class,
			MovementComponent.class,
			BodyComponent.class).get();

	private float accelX = 0.0f;
	private Level level;

	private ComponentMapper<PlayerComponent> rm;
	private ComponentMapper<StateComponent> sm;
	private ComponentMapper<TransformComponent> tm;
	private ComponentMapper<MovementComponent> mm;
	private ComponentMapper<BodyComponent> bm;

	public PlayerSystem(Level level) {
		super(family);

		this.level = level;

		rm = ComponentMapper.getFor(PlayerComponent.class);
		sm = ComponentMapper.getFor(StateComponent.class);
		tm = ComponentMapper.getFor(TransformComponent.class);
		mm = ComponentMapper.getFor(MovementComponent.class);
		bm = ComponentMapper.getFor(BodyComponent.class);
	}

	public void setAccelX(float accelX) {
		this.accelX = accelX;
	}

	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);

		accelX = 0.0f;
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		TransformComponent t = tm.get(entity);
		StateComponent state = sm.get(entity);
		MovementComponent mov = mm.get(entity);
		PlayerComponent player = rm.get(entity);
		BodyComponent body = bm.get(entity);

		int collisionCode = 0;
		if (body.body.getUserData() != null && body.body.getUserData().getClass() == Integer.class) {
			collisionCode = (Integer) body.body.getUserData();
		}

		body.body.setUserData(this);

		// Movement handling
		mov.velocity.x = -accelX / 10.0f * PlayerComponent.VELOCITY_X * deltaTime;
		
		if (state.get() == PlayerComponent.STATE_NORMAL) {
			//goes from 1 to 2 as the level goes on, to make it challenging
			float difficultyFactor = 1 + t.pos.y / Level.LEVEL_HEIGHT;
			mov.velocity.y = PlayerComponent.VELOCITY_Y * deltaTime * difficultyFactor;
		} else if(state.get() == PlayerComponent.STATE_KNOCKED_BACK ){
			mov.velocity.y=PlayerComponent.VELOCITY_Y * deltaTime * (1-6*player.impactCooldown);
			player.impactCooldown-=deltaTime;
			if(player.impactCooldown < 0)
				state.set(PlayerComponent.STATE_NORMAL);
		}

		// Bounds checking
		if (t.pos.x - PlayerComponent.WIDTH / 2 < 0) {
			t.pos.x = PlayerComponent.WIDTH / 2;
		}

		if (t.pos.x + PlayerComponent.WIDTH / 2 > Level.LEVEL_WIDTH) {
			t.pos.x = Level.LEVEL_WIDTH - PlayerComponent.WIDTH / 2;
		}

		//Collision detection
		if(state.get() != PlayerComponent.STATE_KNOCKED_BACK) {
			if (collisionCode == BodyComponent.PLAYER_STRUCTURE_COLLISION) {
				player.currentHealth -= StructureComponent.DAMAGE;
				state.set(PlayerComponent.STATE_KNOCKED_BACK);
				player.impactCooldown=0.4f;
			} else if (collisionCode == BodyComponent.PLAYER_ENEMY_COLLISION) {
				player.currentHealth -= EnemyComponent.DAMAGE;
				state.set(PlayerComponent.STATE_KNOCKED_BACK);
				player.impactCooldown=0.4f;
			}
		}

		// Tilting
		if (mov.velocity.x >= 2f) {
			t.rotation = -0.1f;
		}

		if (mov.velocity.x <= -2f) {
			t.rotation = 0.1f;
		}

		if (mov.velocity.x < 1f && mov.velocity.x > -1f) {
			t.rotation = 0.0f;
		}

		player.heightSoFar = t.pos.y;

		// Death
		if (player.currentHealth <= 0f) {
			level.state = Level.LEVEL_STATE_GAME_OVER;
		}

		//Wgen
		level.generateObstacles(player.heightSoFar, entity);
	}
}
