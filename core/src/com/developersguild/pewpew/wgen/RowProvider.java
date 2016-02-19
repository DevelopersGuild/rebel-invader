package com.developersguild.pewpew.wgen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.RandomXS128;
import com.developersguild.pewpew.Level;

public abstract class RowProvider implements IRowProvider{
	
	protected RandomXS128 rand=new RandomXS128();
	
	public void createRow(Level level, int row, Entity player) {
		rand.setSeed(row * 2293 + 3);
		makeRow(level, row, player);
	}
	
	public TextureRegion pick(TextureRegion[] reg) {
		return reg[rand.nextInt(reg.length)];
	}
	
	protected abstract void makeRow(Level level, int row, Entity player);
}
