package org.chesscorp.club.model;

import org.hibernate.annotations.Proxy;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Robot data model. A robot is a player not referenced by any account but managed by an AI engine.
 *
 * @author Yannick Kirschhoffer <alcibiade@alcibiade.org>
 */
@Entity
@Proxy(lazy = false)
public class Robot extends Player {
    @Column
    private String engine;

    @Column
    private String parameters;
    
    public Robot() {
    }

    public Robot(String displayName, String engine, String parameters) {
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
        return "Robot{" + getDisplayName() + "}";
    }
}
