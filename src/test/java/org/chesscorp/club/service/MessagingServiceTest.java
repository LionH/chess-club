package org.chesscorp.club.service;

import org.chesscorp.club.model.game.ChessGame;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

/**
 * Test message sending.
 */
public class MessagingServiceTest {

    @InjectMocks
    private MessagingServiceImpl messagingService;

    @Mock
    private JmsTemplate jmsTemplate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMessageSending() {
        ChessGame game = Mockito.mock(ChessGame.class);
        Mockito.when(game.getId()).thenReturn(42L);
        messagingService.notifyGameUpdated(game);
        Mockito.verify(jmsTemplate).send(Mockito.anyString(), Mockito.any(MessageCreator.class));
    }
}
