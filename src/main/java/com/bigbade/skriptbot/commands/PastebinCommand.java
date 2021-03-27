package com.bigbade.skriptbot.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import com.bigbade.skriptbot.api.ICommand;
import com.bigbade.skriptbot.utils.HTTPUtilities;
import com.bigbade.skriptbot.utils.MessageUtils;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class PastebinCommand implements ICommand {
    @Getter
    private final List<String> aliases = Collections.unmodifiableList(Arrays.asList("pastebin", "paste"));
    private final String pastebinApiKey;
    private final String prefix;

    @Override
    public void onCommand(Message message, String[] args) {
        if(args.length != 1) {
            MessageUtils.sendEmbedWithReaction(
                    message.getTextChannel(), MessageUtils.getErrorMessage(message.getId(), "Invalid Arguments",
                            "Usage: **" + prefix + "pastebin (message id)**\nTo get a Message ID, enable" +
                                    "developer mode in Discord settings, right click the message, and click \"Copy ID\""));
            return;
        }

        message.getTextChannel().retrieveMessageById(args[0]).onErrorMap(error -> null).queue(fileMessage -> {
            if(fileMessage == null) {
                MessageUtils.sendEmbedWithReaction(message.getTextChannel(), MessageUtils.getErrorMessage(message.getId(),
                        "No Message Found", "There was no message in this channel with that message.getId()."));
                return;
            }
            if(fileMessage.getAttachments().isEmpty()) {
                MessageUtils.sendEmbedWithReaction(message.getTextChannel(), MessageUtils.getErrorMessage(message.getId(),
                        "No File Found", "There was no attached file to that message."));
                return;
            }
            fileMessage.getAttachments().get(0).retrieveInputStream().thenAccept(inputStream -> {
                String input = HTTPUtilities.readInputStream(inputStream);
                sendPastebin(message.getTextChannel(), fileMessage, message.getId(), input);
            });
        });
    }

    private void sendPastebin(TextChannel channel, Message message, String id, String input) {
        Map<String, String> arguments = new HashMap<>();
        arguments.put("api_option", "paste");
        arguments.put("api_paste_private", "1");
        Message.Attachment attachment = message.getAttachments().get(0);
        arguments.put("api_paste_name", message.getAuthor().getName() + "'s File: " + attachment.getFileName());
        arguments.put("api_paste_expire_date", "1D");
        arguments.put("api_dev_key", pastebinApiKey);
        arguments.put("api_paste_format", attachment.getFileExtension());
        arguments.put("api_paste_code", input);
        Optional<InputStream> response =
                HTTPUtilities.sendPostRequest("https://pastebin.com/api/api_post.php", arguments);
        if(!response.isPresent()) {
            MessageUtils.sendEmbedWithReaction(channel, MessageUtils.getErrorMessage(id ,"Error Creating Paste",
                    "An unknown error occurred."));
            return;
        }
        String returned = HTTPUtilities.readInputStream(response.get());
        //Check if it starts with "Bad API response"
        if(returned.charAt(0) == 'B') {
            MessageUtils.sendEmbedWithReaction(channel, MessageUtils.getErrorMessage(id, "Error Creating Paste",
                    "Error: " + returned));
            return;
        }

        MessageUtils.sendEmbedWithReaction(channel, new EmbedBuilder().setTitle("Pastebin'd file")
                .setDescription(returned).setFooter(id).build());
    }
}
