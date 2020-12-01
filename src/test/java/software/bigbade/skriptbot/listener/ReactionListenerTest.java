package software.bigbade.skriptbot.listener;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageHistory;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.SelfUser;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.AuditableRestAction;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.ArgumentCaptor;
import org.mockito.internal.verification.VerificationModeFactory;
import software.bigbade.skriptbot.api.ICommand;
import software.bigbade.skriptbot.listeners.ReactionListener;
import software.bigbade.skriptbot.utils.MessageUtils;

import java.util.Collections;
import java.util.function.Consumer;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReactionListenerTest {
    private static final JDA JDA = mock(JDA.class);
    private static final SelfUser SELF_USER = mock(SelfUser.class);
    private static final ReactionListener REACTION_LISTENER;

    /**
     * Structure:
     * MessageReactionAddEvent
     * - MessageReaction
     *  - ReactionEmote
     *    - String (codepoint)
     *    - boolean (isEmoji)
     * - User (Reactor)
     * - boolean (fromGuild)
     * - RestAction&gt;Message&lt; (Message queue)
     *  - Message (Sent message)
     *   - User (Author)
     *   - TextChannel (Message channel)
     *   - MessageRetrieveAction (Message#getHistoryBefore(Message, 1))
     *    - MessageHistory
     *     - AuditableRestAction (delete)
     *     - Message (List#get(0))
     *      - User (Author)
     *      - String (Content)
     */
    private static final MessageReactionAddEvent REACTION_ADD_EVENT = mock(MessageReactionAddEvent.class);
    private static final MessageReaction MESSAGE_REACTION = mock(MessageReaction.class);
    private static final MessageReaction.ReactionEmote REACTION_EMOTE = mock(MessageReaction.ReactionEmote.class);
    private static final User COMMAND_USER = mock(User.class);
    @SuppressWarnings("unchecked")
    private static final RestAction<Message> MESSAGE_QUEUE = mock(RestAction.class);
    private static final Message REACTED_MESSAGE = mock(Message.class);
    private static final TextChannel MESSAGE_CHANNEL = mock(TextChannel.class);
    private static final MessageHistory.MessageRetrieveAction RETRIEVE_ACTION = mock(MessageHistory.MessageRetrieveAction.class);
    private static final MessageHistory MOCK_HISTORY = mock(MessageHistory.class);
    @SuppressWarnings("unchecked")
    private static final AuditableRestAction<Void> DELETE_MESSAGE = mock(AuditableRestAction.class);
    private static final Message LAST_MESSAGE = mock(Message.class);

    private static boolean addedReaction;

    static {
        ICommand command = new ICommand() {
            @Override
            public void onCommand(TextChannel channel, String id, String[] args) {
                //Not used for the test
            }

            @Override
            public void onReaction(User user, Message command, Message message, MessageReaction.ReactionEmote emote) {
                Assertions.assertEquals("U+0000", emote.getAsCodepoints());
                addedReaction = true;
            }

            @Override
            public String[] getAliases() {
                return new String[]{"test"};
            }
        };
        REACTION_LISTENER = new ReactionListener(Collections.singletonList(command), ".");
    }

    @BeforeAll
    static void setupReactionTest() {
        REACTION_LISTENER.setJda(JDA);
        //Set up event structure
        when(REACTION_ADD_EVENT.getReaction()).thenReturn(MESSAGE_REACTION);
        when(REACTION_ADD_EVENT.getUser()).thenReturn(COMMAND_USER);
        when(MESSAGE_REACTION.getReactionEmote()).thenReturn(REACTION_EMOTE);
        when(REACTION_ADD_EVENT.retrieveMessage()).thenReturn(MESSAGE_QUEUE);
        when(MESSAGE_CHANNEL.getHistoryBefore(REACTED_MESSAGE, 1)).thenReturn(RETRIEVE_ACTION);
        when(REACTED_MESSAGE.getChannel()).thenReturn(MESSAGE_CHANNEL);
        when(REACTED_MESSAGE.delete()).thenReturn(DELETE_MESSAGE);
        when(MOCK_HISTORY.getRetrievedHistory()).thenReturn(Collections.singletonList(LAST_MESSAGE));
        when(LAST_MESSAGE.getContentStripped()).thenReturn(".testing");
        when(LAST_MESSAGE.getAuthor()).thenReturn(COMMAND_USER);

        //Things that need to be tested
        when(JDA.getSelfUser()).thenReturn(SELF_USER);
        when(COMMAND_USER.isBot()).thenReturn(true);
        when(REACTION_ADD_EVENT.isFromGuild()).thenReturn(false);
        when(REACTION_EMOTE.isEmoji()).thenReturn(true);
        when(REACTED_MESSAGE.getAuthor()).thenReturn(COMMAND_USER);
        when(MOCK_HISTORY.isEmpty()).thenReturn(true);
        when(REACTION_EMOTE.getAsCodepoints()).thenReturn(MessageUtils.DELETE_REACTION);
    }

    @BeforeEach
    void beforeEachReactionTest() {
        addedReaction = false;
    }

    @Order(1)
    @Test
    void onWrongMessageReactionAddTest() {
        REACTION_LISTENER.onMessageReactionAdd(REACTION_ADD_EVENT);
        when(REACTION_ADD_EVENT.isFromGuild()).thenReturn(true);
        REACTION_LISTENER.onMessageReactionAdd(REACTION_ADD_EVENT);
        when(COMMAND_USER.isBot()).thenReturn(false);
        REACTION_LISTENER.onMessageReactionAdd(REACTION_ADD_EVENT);
        when(REACTION_EMOTE.isEmoji()).thenReturn(false);
        handleRetrieveMessage(false);
        when(REACTED_MESSAGE.getAuthor()).thenReturn(SELF_USER);

        handleRetrieveMessage(true);
        when(MOCK_HISTORY.isEmpty()).thenReturn(false);
        handleRetrieveMessage(true);
        verify(DELETE_MESSAGE).queue();
        when(REACTION_EMOTE.getAsCodepoints()).thenReturn("U+0000");
        handleRetrieveMessage(true);
        when(LAST_MESSAGE.getContentStripped()).thenReturn("test");
        handleRetrieveMessage(true);
        Assertions.assertFalse(addedReaction);
    }

    @Order(2)
    @Test
    void onCorrectMessageReactionAddTest() {
        when(LAST_MESSAGE.getContentStripped()).thenReturn(".test");
        handleRetrieveMessage(true);
        Assertions.assertTrue(addedReaction);
    }

    @SuppressWarnings("unchecked")
    private void handleRetrieveMessage(boolean handleHistory) {
        ArgumentCaptor<Consumer<? super Message>> messageCaptor = ArgumentCaptor.forClass(Consumer.class);
        ArgumentCaptor<Consumer<? super MessageHistory>> historyCaptor = null;
        if(handleHistory) {
            historyCaptor = ArgumentCaptor.forClass(Consumer.class);
        }
        REACTION_LISTENER.onMessageReactionAdd(REACTION_ADD_EVENT);
        verify(MESSAGE_QUEUE, VerificationModeFactory.atLeast(1)).queue(messageCaptor.capture());
        messageCaptor.getValue().accept(REACTED_MESSAGE);
        if(handleHistory) {
            verify(RETRIEVE_ACTION, VerificationModeFactory.atLeast(1)).queue(historyCaptor.capture());
            historyCaptor.getValue().accept(MOCK_HISTORY);
        }
    }
}
