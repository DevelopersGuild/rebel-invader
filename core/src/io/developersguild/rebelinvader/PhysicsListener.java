package io.developersguild.rebelinvader;

import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntityListener;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

import io.developersguild.rebelinvader.components.BodyComponent;
import io.developersguild.rebelinvader.components.BulletComponent;
import io.developersguild.rebelinvader.components.MissileComponent;
import io.developersguild.rebelinvader.components.PlayerComponent;
import io.developersguild.rebelinvader.systems.EnemySystem;
import io.developersguild.rebelinvader.systems.ExplosionSystem;
import io.developersguild.rebelinvader.systems.PlayerSystem;
import io.developersguild.rebelinvader.systems.StructureSystem;

/**
 * Created by Vihan on 1/30/2016.
 */
public class PhysicsListener implements ContactListener, EntityListener {
    /**
     * Called when two fixtures begin to touch.
     *
     * @param contact
     */
    @Override
    public void beginContact(Contact contact) {
        //Gdx.app.log(this.getClass().getSimpleName(), "beginContact() called");

        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        // If a is an explosion
        if (a.getUserData().getClass() == ExplosionSystem.class) {
            // If b is a structure
            if (b.getUserData().getClass() == StructureSystem.class) {
                b.setUserData(BodyComponent.EXPLOSION_STRUCTURE_COLLISION);
            }
            // If b is an enemy
            if (b.getUserData().getClass() == EnemySystem.class) {
                b.setUserData(BodyComponent.EXPLOSION_ENEMY_COLLISION);
            }
        }

        // If b is an explosion
        if (b.getUserData().getClass() == ExplosionSystem.class) {
            // If b is a structure
            if (a.getUserData().getClass() == StructureSystem.class) {
                a.setUserData(BodyComponent.EXPLOSION_STRUCTURE_COLLISION);
            }
            // If b is an enemy
            if (a.getUserData().getClass() == EnemySystem.class) {
                a.setUserData(BodyComponent.EXPLOSION_ENEMY_COLLISION);
            }
        }

        if (b.getUserData() instanceof Entity) {
            if (((Entity) b.getUserData()).getComponent(BulletComponent.class) != null ||
                    ((Entity) b.getUserData()).getComponent(MissileComponent.class) != null) {
                Body c = a;
                a = b;
                b = c;
            }
        }

        // If a is a bullet
        if (a.getUserData() instanceof Entity) {
            // If bullet came from a player
            if (((Entity) a.getUserData()).getComponent(BulletComponent.class) != null) {
                if (((Entity) a.getUserData()).getComponent(BulletComponent.class).origin.getComponent(PlayerComponent.class) != null) {
                    // If b is a structure
                    if (b.getUserData().getClass() == StructureSystem.class) {
                        a.setUserData(BodyComponent.BULLET_STRUCTURE_COLLISION);
                        b.setUserData(BodyComponent.BULLET_STRUCTURE_COLLISION);
                    }

                    // If b is an enemy
                    if (b.getUserData().getClass() == EnemySystem.class) {
                        a.setUserData(BodyComponent.BULLET_ENEMY_COLLISION);
                        b.setUserData(BodyComponent.BULLET_ENEMY_COLLISION);
                    }
                } else { // If bullet came from anyone except player
                    // If b is a player
                    if (b.getUserData().getClass() == PlayerSystem.class) {
                        a.setUserData(BodyComponent.PLAYER_BULLET_COLLISION);
                        b.setUserData(BodyComponent.PLAYER_BULLET_COLLISION);
                    }
                }
                return;
            }

            // If missile came from a player
            if (((Entity) a.getUserData()).getComponent(MissileComponent.class) != null) {
                if (((Entity) a.getUserData()).getComponent(MissileComponent.class).origin.getComponent(PlayerComponent.class) != null) {
                    // If b is a structure
                    if (b.getUserData().getClass() == StructureSystem.class) {
                        a.setUserData(BodyComponent.MISSILE_STRUCTURE_COLLISION);
                        b.setUserData(BodyComponent.MISSILE_STRUCTURE_COLLISION);
                    }

                    // If b is an enemy
                    if (b.getUserData().getClass() == EnemySystem.class) {
                        a.setUserData(BodyComponent.MISSILE_ENEMY_COLLISION);
                        b.setUserData(BodyComponent.MISSILE_ENEMY_COLLISION);
                    }
                }
            }
        }

        /*
        if (b.getUserData() instanceof Entity) {
            // If bullet came from a player
            if (((Entity) b.getUserData()).getComponent(BulletComponent.class).origin.getComponent(PlayerComponent.class) != null) {
                // If a is a structure
                if (a.getUserData().getClass() == StructureSystem.class) {
                    a.setUserData(BodyComponent.BULLET_STRUCTURE_COLLISION);
                    b.setUserData(BodyComponent.BULLET_STRUCTURE_COLLISION);
                }

                // If a is an enemy
                if (a.getUserData().getClass() == EnemySystem.class) {
                    a.setUserData(BodyComponent.BULLET_ENEMY_COLLISION);
                    b.setUserData(BodyComponent.BULLET_ENEMY_COLLISION);
                }
            } else { // If bullet came from anyone except player
                // If a is a player
                if (a.getUserData().getClass() == PlayerSystem.class) {
                    a.setUserData(BodyComponent.PLAYER_BULLET_COLLISION);
                    b.setUserData(BodyComponent.PLAYER_BULLET_COLLISION);
                }
            }
        }
        */


    }

    /**
     * Called when two fixtures cease to touch.
     *
     * @param contact
     */
    @Override
    public void endContact(Contact contact) {
        //Gdx.app.log(this.getClass().getSimpleName(), "endContact() called");

    }

    /**
     * Called every time step while the fixtures are overlapping
     * Gives you a chance to alter the contact before it is processed by the collision response
     *
     * @param contact
     * @param oldManifold
     */
    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        //Gdx.app.log(this.getClass().getSimpleName(), "preSolve() called");
    }

    /**
     * Called every time step while the fixtures are overlapping
     * Gives you a chance to find out what impulses were caused by the collision response after it has been applied
     *
     * @param contact
     * @param impulse
     */
    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        //Gdx.app.log(this.getClass().getSimpleName(), "postSolve() called");

        Body a = contact.getFixtureA().getBody();
        Body b = contact.getFixtureB().getBody();

        // If a is a player
        if (a.getUserData().getClass() == PlayerSystem.class) {
            // If b is a structure
            if (b.getUserData().getClass() == StructureSystem.class) {
                a.setUserData(BodyComponent.PLAYER_STRUCTURE_COLLISION);
            }
            // If b is an enemy
            if (b.getUserData().getClass() == EnemySystem.class) {
                a.setUserData(BodyComponent.PLAYER_ENEMY_COLLISION);
            }
        }

        // If b is a player
        if (b.getUserData().getClass() == PlayerSystem.class) {
            // If a is a structure
            if (a.getUserData().getClass() == StructureSystem.class) {
                b.setUserData(BodyComponent.PLAYER_STRUCTURE_COLLISION);
            }
            // If a is an enemy
            if (a.getUserData().getClass() == EnemySystem.class) {
                b.setUserData(BodyComponent.PLAYER_ENEMY_COLLISION);
            }
        }
    }

    /**
     * Called whenever an {@link Entity} is added to {@link Engine} or a specific {@link Family} See
     * {@link Engine#addEntityListener(EntityListener)} and {@link Engine#addEntityListener(Family, EntityListener)}
     *
     * @param entity
     */
    @Override
    public void entityAdded(Entity entity) {
        //Gdx.app.log(this.getClass().getSimpleName(), "entityAdded() called");
    }

    /**
     * Called whenever an {@link Entity} is removed from {@link Engine} or a specific {@link Family} See
     * {@link Engine#addEntityListener(EntityListener)} and {@link Engine#addEntityListener(Family, EntityListener)}
     *
     * @param entity
     */
    @Override
    public void entityRemoved(Entity entity) {
        if (entity.getComponent(BulletComponent.class) != null) {
        }
    }
}