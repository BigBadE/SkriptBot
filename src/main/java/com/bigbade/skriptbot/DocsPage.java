package com.bigbade.skriptbot;

import com.bigbade.skriptbot.commands.DocSearchCommand;
import com.bigbade.skriptbot.utils.MessageUtils;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;

import java.util.concurrent.atomic.AtomicInteger;

@RequiredArgsConstructor
public class DocsPage {
    public static final String ARROW_LEFT = "U+25C0";
    public static final String ARROW_RIGHT = "U+25B6";

    private final int number;
    private final int max;

    public void addReactions(Message message) {
        queNextAction(message, new AtomicInteger(0), Math.min(max, 10));
    }

    public boolean isLeft(String emote) {
        return emote.equalsIgnoreCase(ARROW_LEFT) && number >= 10;
    }

    public boolean isRight(String emote) {
        return emote.equalsIgnoreCase(ARROW_RIGHT) && number < max - 10;
    }

    public void scroll(Message message, User reactor, String emote) {
        if (isLeft(emote)) {
            message.removeReaction(ARROW_LEFT, reactor).queue(DocSearchCommand::ignoreInput,
                    DocSearchCommand::ignoreInput);
        } else if (isRight(emote)) {
            message.removeReaction(ARROW_RIGHT, reactor).queue(DocSearchCommand::ignoreInput,
                    DocSearchCommand::ignoreInput);
        }
    }

    private static void queNextAction(Message message, AtomicInteger current, int max) {
        String reaction;
        int value = current.getAndIncrement();
        if (value >= max && value < 11) {
            current.set(11);
            value = 10;
        }
        if (value < 10) {
            reaction = getNumberEmote(value);
        } else if (value == 10) {
            reaction = MessageUtils.DELETE_REACTION;
        } else if (value == 11) {
            reaction = ARROW_LEFT;
        } else if (value == 12) {
            reaction = ARROW_RIGHT;
        } else {
            return;
        }

        message.addReaction(reaction).queue(none -> queNextAction(message, current, max),
                DocSearchCommand::ignoreInput);
    }

    public static String getNumberEmote(int number) {
        if (number < 0 || number > 9) {
            throw new IllegalArgumentException("Number outside of range [0, 9]");
        }
        return "U+3" + number + "U+fe0fU+20e3";
    }
}
