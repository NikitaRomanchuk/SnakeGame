package ru.itis.server;

public class Protocol {
    public static final int MAX_ACTION_LENGTH = 1024;

    public static final int SEND_ERROR = 0;
    public static final byte JOIN = 1;
    public static final byte ORDER = 2;
    public static final byte LOSE = 3;
    public static final byte MAP_DATA = 4;
}
