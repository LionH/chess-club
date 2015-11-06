package org.chesscorp.club.model.people;

import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * RobotPlayer data model. A robot is a player not referenced by any account but managed by an AI engine.
 *
 * @author Yannick Kirschhoffer <alcibiade@alcibiade.org>
 */
@Entity
@Proxy(lazy = false)
public class RobotPlayer extends Player {
    @Column(length = 16)
    private String engine;

    @Column(length = 64)
    private String parameters;

    public RobotPlayer() {
    }

    public RobotPlayer(String displayName, String engine, String parameters) {
        super(displayName);
        this.engine = engine;
        this.parameters = parameters;
    }

    public String getEngine() {
        return engine;
    }

    public String getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "RobotPlayer{" + getDisplayName() + "}";
    }
}
