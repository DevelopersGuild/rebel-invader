package com.developersguild.pewpew.wgen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.RandomXS128;
import com.developersguild.Debug;
import com.developersguild.pewpew.Assets;
import com.developersguild.pewpew.Level;
import com.developersguild.pewpew.components.PlayerComponent;
import com.developersguild.pewpew.components.StructureComponent;

public class WorldGenerator implements IRowProvider {

	private final Level level;
	
	private final int seed;

	public WorldGenerator(Level level) {
		this.level = level;
		seed=(int) (Math.random()*Integer.MAX_VALUE);
	}

	public void provideWorld(float heightNeeded, Entity player) {
		provideWorldNew(heightNeeded, player);
	}

	//New-style wgen

	private RandomXS128 r=new RandomXS128();
	//Coherent random numbers
	public float getSliding5Random(int row){
		float sum=0;
		for(int i=0; i<5; i++){
			r.setSeed(row+i+seed);
			sum += r.nextFloat();
		}
		return sum;
	}

	//Gets a density, weighting towards higher as height goes up
	public float getDensity(int height){
		return getSliding5Random(height) + height*0.1f;
	}

	public IRowProvider[] providers=new IRowProvider[]{
			new RowProviderSpaceJunk(),
			new RowProviderSpaceJunk(),
			new RowProviderSpaceJunk(),
			new RowProviderSpaceJunk(),
			new RowProviderSpaceJunk(),
			new RowProviderSpaceJunk(),
			new RowProviderSpaceJunk(),
			new RowProviderSpaceJunk(),
			new RowProviderWall(),
			new RowProviderAlienHabitat(),
			new RowProviderAlienHabitat(),
			new RowProviderAlienHabitat(),
			new RowProviderAlienHabitat(),
			new RowProviderAlienHabitat(),
			//TODO order the world providers - this determines world generation
	};
	public IRowProvider getProvider(int height){
		int density=(int) getDensity(height);
		//Bounds checking
		if(density >= providers.length){
			density=providers.length-1;
		}
		return providers[density];
	}
	int lastHeight=8;
	public void provideWorldNew(float height, Entity player){
		int heightAsInt=(int) ((height+Level.SCREEN_HEIGHT*1.2)/StructureComponent.HEIGHT);
		if(heightAsInt>=lastHeight+1){ //assumes we never miss more than one row
			lastHeight++;
			getProvider(heightAsInt).createRow(level, heightAsInt, player);
		}
	}



	//Old-style wgen
	private float path = 5.0f;
	private float lastDeltaPath = 0.0f;

	@Override
	public void createRow(Level level, int row, Entity player) {
		float baseHeight=row*StructureComponent.HEIGHT;
		float restrictedArea =
				PlayerComponent.WIDTH * 1.3f        //Generous width
				+ lastDeltaPath / 2;            //Plus more if the path is changing sharply, so you can still get through
		for (int i = 0; i <= Level.LEVEL_WIDTH / StructureComponent.WIDTH + 1; i++) {
			float x = i * StructureComponent.WIDTH;
			//Check that we're not stomping the path
			if ((x > path + restrictedArea || x < path - restrictedArea)) {
				if (this.level.rand.nextFloat() < 0.2)
					this.level.createStructure(x, baseHeight, player, Assets.cityRegions[0]);
			}
		}

		//Generate enemy
		if (this.level.rand.nextFloat() < 0.1) {
			this.level.createEnemy(path, baseHeight, player, this.level.rand.nextInt(Assets.ENEMY_SPRITES));
		}

		//Move the clear path so you can't just fly in a straight line
		path += lastDeltaPath;

		lastDeltaPath += (this.level.rand.nextFloat() - 0.5f)//random-walk term, evenly distributed in +-0.5
				* (1 + baseHeight / Level.LEVEL_HEIGHT);            //plus a difficulty scaling term

		//If the path is outside the world, move it back in, and make it go the other way
		if (path < 0) {
			path = 0.2f;
			lastDeltaPath *= -1;
		} else if (path > Level.LEVEL_WIDTH) {
			path = Level.LEVEL_WIDTH - 0.2f;
			lastDeltaPath *= -1;
		}
	}
}