package com.developersguild.pewpew.wgen;

import com.badlogic.ashley.core.Entity;
import com.developersguild.pewpew.Level;

public interface IRowProvider {
	
	public void createRow(Level level, int row, Entity player);

}
