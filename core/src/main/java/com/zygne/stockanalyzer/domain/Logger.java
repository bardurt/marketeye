package com.zygne.stockanalyzer.domain;

public interface Logger {

    void shutDown();

    void setUp();

    void log(LOG_LEVEL level, String message);

    void clear();

    public enum LOG_LEVEL {
        DEBUG,
        ERROR,
        WARNING,
        INFO
    }

    public class Command {

        public static class Log extends Command {
            private LOG_LEVEL level;
            private String message;

            public Log(LOG_LEVEL level, String message) {
                this.level = level;
                this.message = message;
            }

            public LOG_LEVEL getLevel() {
                return level;
            }

            public void setLevel(LOG_LEVEL level) {
                this.level = level;
            }

            public String getMessage() {
                return message;
            }

            public void setMessage(String message) {
                this.message = message;
            }
        }

        public static class Clear extends Command {
        }

        public static class ShutDown extends Command {
        }
    }
}
