package com.bigbade.skriptbot.command;

import com.bigbade.skriptbot.BasicCommandTestSetup;
import com.bigbade.skriptbot.commands.DownloadCommand;
import com.bigbade.skriptbot.testutils.TestMessage;
import com.bigbade.skriptbot.testutils.TestResourceDataFetcher;
import com.github.cliftonlabs.json_simple.Jsonable;
import com.github.cliftonlabs.json_simple.Jsoner;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.EmbedBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DownloadCommandTest extends BasicCommandTestSetup<DownloadCommand> {
    private static final String TEST_JSON = "{\"tag_name\":\"test_version\",\"html_url\":\"correct!\"}";

    private final TestResourceDataFetcher dataFetcher = new TestResourceDataFetcher();

    @Test
    void testDownloadCommand() {
        TestMessage testMessage = new TestMessage(".download", getTestChannel());
        EmbedBuilder downloadEmbed = getCommand().updateEmbedMessage();
        String testField = downloadEmbed.getFields().get(2).getValue();

        assert testField != null;
        String[] split = testField.split("\\)]\\(");
        Assertions.assertTrue(split[0].endsWith("test_version"));
        Assertions.assertTrue(split[1].startsWith("correct!"));
        getTestChannel().expectMessage(new TestMessage(new EmbedBuilder(downloadEmbed)
                .setFooter("Downloads | " + testMessage.getId()).build(), getTestChannel()));
        sendMessage(testMessage);
        verify();
    }

    @SneakyThrows
    @Override
    public DownloadCommand getTestedCommand() {
        dataFetcher.setData((Jsonable) Jsoner.deserialize(TEST_JSON));
        return new DownloadCommand(dataFetcher);
    }
}
