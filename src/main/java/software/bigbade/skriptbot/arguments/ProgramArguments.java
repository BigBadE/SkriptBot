package software.bigbade.skriptbot.arguments;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProgramArguments {
    private final boolean debugMode;
    private final String pastebinKey;
    private final String skUnityKey;
    private final String token;
    @SuppressWarnings({"FieldMayBeFinal", "UnusedAssignment"})
    @Builder.Default
    private String prefix = ".";
}
