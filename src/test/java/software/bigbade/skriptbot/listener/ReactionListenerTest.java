package software.bigbade.skriptbot.listener;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import software.bigbade.skriptbot.api.ICommand;
import software.bigbade.skriptbot.listeners.ReactionListener;
import software.bigbade.skriptbot.testutils.TestChannel;
import software.bigbade.skriptbot.testutils.TestIDHandler;
import software.bigbade.skriptbot.testutils.TestMessage;
import software.bigbade.skriptbot.testutils.TestUser;
import software.bigbade.skriptbot.utils.MessageUtils;

import java.util.Collections;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReactionListenerTest {
    private static final JDA JDA = TestChannel.TEST_JDA;

    private static final ReactionListener REACTION_LISTENER;

    private static MessageReaction.ReactionEmote REACTION_EMOTE = MessageReaction.ReactionEmote.fromUnicode(
            TestMessage.hexToName(MessageUtils.DELETE_REACTION), JDA);
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
    private static final TestChannel MESSAGE_CHANNEL = new TestChannel();
    private static final MessageReaction MESSAGE_REACTION = new MessageReaction(MESSAGE_CHANNEL, REACTION_EMOTE,
            TestIDHandler.getId(), false, 1);
    private static final MessageReactionAddEvent REACTION_ADD_EVENT = new MessageReactionAddEvent(JDA,
            TestIDHandler.getId(), JDA.getSelfUser(), null, MESSAGE_REACTION, TestIDHandler.getId());

    private static final TestUser COMMAND_USER = new TestUser("TestReactionUser");
    private static final TestMessage REACTED_MESSAGE = new TestMessage("TestMessage", MESSAGE_CHANNEL);
    private static TestMessage LAST_MESSAGE = new TestMessage(".testing", MESSAGE_CHANNEL);

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

        MESSAGE_CHANNEL.setType(ChannelType.TEXT);

        LAST_MESSAGE.setAuthor((TestUser) JDA.getSelfUser());
        REACTED_MESSAGE.setAuthor(COMMAND_USER);
    }

    @BeforeEach
    void beforeEachReactionTest() {
        addedReaction = false;
    }

    @Order(1)
    @Test
    void onWrongMessageReactionAddTest() {
        MESSAGE_CHANNEL.setType(ChannelType.PRIVATE);
        COMMAND_USER.setBot(true);
        REACTED_MESSAGE.setAuthor((TestUser) JDA.getSelfUser());
        REACTION_LISTENER.onMessageReactionAdd(REACTION_ADD_EVENT);
        MESSAGE_CHANNEL.setType(ChannelType.TEXT);
        REACTION_LISTENER.onMessageReactionAdd(REACTION_ADD_EVENT);
        COMMAND_USER.setBot(false);

        REACTION_LISTENER.onMessageReactionAdd(REACTION_ADD_EVENT);
        REACTED_MESSAGE.setAuthor(COMMAND_USER);

        REACTION_LISTENER.onMessageReactionAdd(REACTION_ADD_EVENT);
        MESSAGE_CHANNEL.setRetrievedMessage(LAST_MESSAGE);

        REACTION_LISTENER.onMessageReactionAdd(REACTION_ADD_EVENT);
        REACTED_MESSAGE.verify(true);

        REACTION_EMOTE = MessageReaction.ReactionEmote.fromUnicode(TestMessage.hexToName("U+0000"), JDA);
        REACTION_LISTENER.onMessageReactionAdd(REACTION_ADD_EVENT);
        LAST_MESSAGE = new TestMessage("test", MESSAGE_CHANNEL);
        LAST_MESSAGE.setAuthor((TestUser) JDA.getSelfUser());

        REACTION_LISTENER.onMessageReactionAdd(REACTION_ADD_EVENT);
        Assertions.assertFalse(addedReaction);
    }

    @Order(2)
    @Test
    void onCorrectMessageReactionAddTest() {
        LAST_MESSAGE = new TestMessage(".test", MESSAGE_CHANNEL);
        LAST_MESSAGE.setAuthor((TestUser) JDA.getSelfUser());
        REACTION_LISTENER.onMessageReactionAdd(REACTION_ADD_EVENT);
        Assertions.assertTrue(addedReaction);
    }
}
