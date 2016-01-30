package com.developersguild.pewpew;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

/**
 * Created by Vihan on 1/30/2016.
 */
public class PhysicsListener implements ContactListener, EntityListener {
    @Override
    public void beginContact(Contact contact) {
        Gdx.app.log(this.getClass().getSimpleName(), "beginContact() called");
    }

    @Override
    public void endContact(Contact contact) {
        Gdx.app.log(this.getClass().getSimpleName(), "endContact() called");
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        //Gdx.app.log(this.getClass().getSimpleName(), "preSolve() called");
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        //Gdx.app.log(this.getClass().getSimpleName(), "postSolve() called");
    }

    @Override
    public void entityAdded(Entity entity) {
        Gdx.app.log(this.getClass().getSimpleName(), "entityAdded() called");
    }

    @Override
    public void entityRemoved(Entity entity) {
        Gdx.app.log(this.getClass().getSimpleName(), "entityRemoved() called");
    }
}
