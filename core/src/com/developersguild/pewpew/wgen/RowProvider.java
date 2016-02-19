package com.developersguild.pewpew.wgen;

import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.math.RandomXS128;
import com.developersguild.pewpew.Level;

public abstract class RowProvider implements IRowProvider{
	
	public void createRow(Level level, int row, Entity player) {
		createRow(level, row, new RandomXS128(row * 2293 + 3));
	}
	
	public abstract void createRow(Level level, int row, RandomXS128 rand);
}
