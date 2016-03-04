package io.developersguild.rebelinvader.wgen;

import com.badlogic.ashley.core.Entity;
import io.developersguild.rebelinvader.Level;

public interface IRowProvider {

    public void createRow(Level level, int row, Entity player);

}
