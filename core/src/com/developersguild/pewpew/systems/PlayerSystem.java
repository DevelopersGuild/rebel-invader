package com.developersguild.pewpew.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.graphics.Color;
import com.developersguild.pewpew.Level;
import com.developersguild.pewpew.components.BodyComponent;
import com.developersguild.pewpew.components.EnemyComponent;
import com.developersguild.pewpew.components.MovementComponent;
import com.developersguild.pewpew.components.PlayerComponent;
import com.developersguild.pewpew.components.StateComponent;
import com.developersguild.pewpew.components.StructureComponent;
import com.developersguild.pewpew.components.TextureComponent;
import com.developersguild.pewpew.components.TransformComponent;

/**
 * Created by Vihan on 1/10/2016.
 */
public class PlayerSystem extends IteratingSystem {
	private static final Family family = Family.all(PlayerComponent.class,
			StateComponent.class,
			TransformComponent.class,
			MovementComponent.class,
			BodyComponent.class,
			TextureComponent.class).get();

	private float accelX = 0.0f;
	private Level level;

	private ComponentMapper<PlayerComponent> rm;
	private ComponentMapper<StateComponent> sm;
	private ComponentMapper<TransformComponent> tm;
	private ComponentMapper<MovementComponent> mm;
	private ComponentMapper<BodyComponent> bm;
	private ComponentMapper<TextureComponent> texm;

	public PlayerSystem(Level level) {
		super(family);

		this.level = level;

		rm = ComponentMapper.getFor(PlayerComponent.class);
		sm = ComponentMapper.getFor(StateComponent.class);
		tm = ComponentMapper.getFor(TransformComponent.class);
		mm = ComponentMapper.getFor(MovementComponent.class);
		bm = ComponentMapper.getFor(BodyComponent.class);
		texm=ComponentMapper.getFor(TextureComponent.class);
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
		TextureComponent tex=texm.get(entity);

		int collisionCode = 0;
		if (body.body.getUserData() != null && body.body.getUserData().getClass() == Integer.class) {
			collisionCode = (Integer) body.body.getUserData();
		}

		body.body.setUserData(this);

		// Movement handling
		{
			mov.velocity.x = -accelX / 10.0f * PlayerComponent.VELOCITY_X * deltaTime;

			float difficultyFactor = 1 + t.pos.y / Level.LEVEL_HEIGHT;
			mov.velocity.y = PlayerComponent.VELOCITY_Y * deltaTime * difficultyFactor;

			if(state.get() == PlayerComponent.STATE_KNOCKED_BACK ){
				mov.velocity.y-= 
						player.impactCooldown
						/PlayerComponent.KNOCKBACK_DURATION 
						* PlayerComponent.VELOCITY_Y
						* 0.5;
				
				player.impactCooldown-=deltaTime;
				if(player.impactCooldown < 0)
					state.set(PlayerComponent.STATE_NORMAL);
			}
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
				player.impactCooldown=PlayerComponent.KNOCKBACK_DURATION;
			} else if (collisionCode == BodyComponent.PLAYER_ENEMY_COLLISION) {
				player.currentHealth -= EnemyComponent.DAMAGE;
				state.set(PlayerComponent.STATE_KNOCKED_BACK);
				player.impactCooldown=PlayerComponent.KNOCKBACK_DURATION;
			}
		}
		
		//Blink after being knocked back
		if(state.get() == PlayerComponent.STATE_KNOCKED_BACK) {
			tex.color=Color.RED;//.lerp(Color.WHITE, player.impactCooldown/PlayerComponent.KNOCKBACK_DURATION);
		} else {
			tex.color=Color.WHITE;
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

		player.heightSoFar = Math.max(t.pos.y, player.heightSoFar);

		// Death
		if (player.currentHealth <= 0f) {
			level.state = Level.LEVEL_STATE_GAME_OVER;
		}

		//Wgen
		level.generateObstacles(player.heightSoFar, entity);
	}
}
