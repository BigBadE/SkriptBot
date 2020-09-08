package software.bigbade.skriptbot.arguments;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProgramArguments {
    private final boolean debugMode;
    private final String skUnityToken;
    private final String token;
    @SuppressWarnings("FieldMayBeFinal")
    @Builder.Default
    private String prefix = ".";
}
