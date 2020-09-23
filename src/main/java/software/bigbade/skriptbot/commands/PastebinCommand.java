package software.bigbade.skriptbot.commands;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import software.bigbade.skriptbot.api.ICommand;
import software.bigbade.skriptbot.utils.HTTPUtilities;
import software.bigbade.skriptbot.utils.MessageUtils;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
public class PastebinCommand implements ICommand {
    @Getter
    private final String[] aliases = new String[] { "pastebin", "paste" };
    private final String pastebinApiKey;
    private final String prefix;

    @Override
    public void onCommand(TextChannel channel, String[] args) {
        if(args.length != 1) {
            MessageUtils.sendEmbedWithReaction(
                    channel, MessageUtils.getErrorMessage("Invalid Arguments",
                            "Usage: **" + prefix + "pastebin (message id)**\nTo get a Message ID, enable" +
                                    "developer mode in Discord settings, right click the message, and click \"Copy ID\""));
            return;
        }

        channel.retrieveMessageById(args[0]).onErrorMap(error -> null).queue(message -> {
            if(message == null) {
                MessageUtils.sendEmbedWithReaction(channel, MessageUtils.getErrorMessage("No Message Found",
                        "There was no message in this channel with that id."));
                return;
            }
            if(message.getAttachments().isEmpty()) {
                MessageUtils.sendEmbedWithReaction(channel, MessageUtils.getErrorMessage("No File Found",
                        "There was no attached file to that message."));
                return;
            }
            message.getAttachments().get(0).retrieveInputStream().thenAccept(inputStream -> {
                String input = HTTPUtilities.readInputStream(inputStream);
                sendPastebin(channel, message, input);
            });
        });
    }

    private void sendPastebin(TextChannel channel, Message message, String input) {
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
            MessageUtils.sendEmbedWithReaction(channel, MessageUtils.getErrorMessage("Error Creating Paste",
                    "An unknown error occurred."));
            return;
        }
        String returned = HTTPUtilities.readInputStream(response.get());
        //Check if it starts with "Bad API response"
        if(returned.charAt(0) == 'B') {
            MessageUtils.sendEmbedWithReaction(channel, MessageUtils.getErrorMessage("Error Creating Paste",
                    "Error: " + returned));
            return;
        }

        MessageUtils.sendEmbedWithReaction(channel, new EmbedBuilder().setTitle("Pastebin'd file")
                .setDescription(returned).build());
    }
}
