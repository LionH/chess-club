package org.chesscorp.club.service;

import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Component
public class ChessGameServiceImpl implements ChessGameService {
    @PersistenceContext
    private EntityManager em;
}
