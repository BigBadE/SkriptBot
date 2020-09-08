package software.bigbade.skriptbot.utils;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.awt.Color;
import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MessageUtilsTest {
    @SuppressWarnings("unchecked")
    private static final RestAction<Void> MOCK_REST_ACTION = mock(RestAction.class);
    private static final MessageAction MOCK_MESSAGE_ACTION = mock(MessageAction.class);
    private static final MessageEmbed MOCK_EMBED = mock(MessageEmbed.class);
    private static final TextChannel MOCK_CHANNEL = mock(TextChannel.class);
    private static final Message MOCK_MESSAGE = mock(Message.class);


    @BeforeAll
    static void setupMocks() {
        when(MOCK_CHANNEL.sendMessage(MOCK_EMBED)).thenReturn(MOCK_MESSAGE_ACTION);
        when(MOCK_MESSAGE.addReaction(MessageUtils.DELETE_REACTION)).thenReturn(MOCK_REST_ACTION);
    }

    @SuppressWarnings("unchecked")
    @Test
    void testSendEmbedWithReaction() {
        ArgumentCaptor<Consumer<Message>> captor = ArgumentCaptor.forClass(Consumer.class);
        MessageUtils.sendEmbedWithReaction(MOCK_CHANNEL, MOCK_EMBED);
        verify(MOCK_MESSAGE_ACTION).queue(captor.capture());
        captor.getValue().accept(MOCK_MESSAGE);
        verify(MOCK_REST_ACTION).queue();
    }

    @Test
    void testGetErrorMessage() {
        MessageEmbed embed = MessageUtils.getErrorMessage("title", "description");
        Assertions.assertEquals("title", embed.getTitle());
        Assertions.assertEquals("description", embed.getDescription());
        Assertions.assertEquals(Color.RED, embed.getColor());
        Assertions.assertNotNull(embed.getThumbnail());
        Assertions.assertEquals("https://i.imgur.com/AjlWaz5.png", embed.getThumbnail().getUrl());
        Assertions.assertNotNull(embed.getFooter());
        Assertions.assertEquals("Error", embed.getFooter().getText());
    }
}
