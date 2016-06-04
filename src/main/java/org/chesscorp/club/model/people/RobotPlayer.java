package org.chesscorp.club.model.people;

import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.time.OffsetDateTime;

/**
 * RobotPlayer data model. A robot is a player not referenced by any account but managed by an AI engine.
 *
 * @author Yannick Kirschhoffer <alcibiade@alcibiade.org>
 */
@Entity
@Proxy(lazy = false)
public class RobotPlayer extends RegisteredPlayer {
    @Column(length = 16)
    private String engine;

    @Column(length = 64)
    private String parameters;

    @Column
    private boolean cacheable;

    public RobotPlayer() {
    }

    public RobotPlayer(String displayName, String engine, String parameters) {
        this(displayName, engine, parameters, false);
    }

    public RobotPlayer(String displayName, String engine, String parameters, boolean cacheable) {
        super(displayName, OffsetDateTime.now());
        this.engine = engine;
        this.parameters = parameters;
        this.cacheable = cacheable;
    }

    public String getEngine() {
        return engine;
    }

    public String getParameters() {
        return parameters;
    }

    public boolean isCacheable() {
        return cacheable;
    }

    @Override
    public String toString() {
        return "RobotPlayer{" + getDisplayName() + "}";
    }
}
