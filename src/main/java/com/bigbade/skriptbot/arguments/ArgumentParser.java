package com.bigbade.skriptbot.arguments;

import lombok.Getter;

public class ArgumentParser {
    @Getter
    private final ProgramArguments arguments;

    public ArgumentParser(String[] args) {
        ProgramArguments.ProgramArgumentsBuilder builder = ProgramArguments.builder();
        int i = 0;
        while (i < args.length) {
            switch (args[i++].toLowerCase()) {
                case "--token":
                    checkArgumentLength(args, i);
                    builder.token(args[i++]);
                    break;
                case "--debug":
                    builder.debugMode(true);
                    break;
                case "--prefix":
                    checkArgumentLength(args, i);
                    builder.prefix(args[i++]);
                    break;
                case "--skunitykey":
                    checkArgumentLength(args, i);
                    builder.skUnityKey(args[i++]);
                    break;
                case "--pastebinkey":
                    checkArgumentLength(args, i);
                    builder.pastebinKey(args[i++]);
                    break;
                default:
                    throw new IllegalArgumentException("Unknown argument: " + args[i-1]);
            }
        }
        arguments = builder.build();
        if(arguments.getToken() == null) {
            throw new IllegalArgumentException("Empty token");
        }
    }

    private static void checkArgumentLength(String[] args, int index) {
        if(args.length <= index) {
            throw new IllegalArgumentException("Argument " + args[index-1] + " has no value");
        }
    }
}
