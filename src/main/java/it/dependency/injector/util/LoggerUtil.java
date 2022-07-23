package it.dependency.injector.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerUtil {
    private Logger _log;
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_BOLD = "\u001B[1m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_WHITE = "\u001B[37m";

    public LoggerUtil(String name){
        _log = Logger.getLogger(name);
    }

    public void info(String msg){
        _log.info(ANSI_WHITE + msg + ANSI_RESET);
    }
    public void warn(String msg){
        _log.warning(ANSI_YELLOW + msg + ANSI_RESET);
    }
    public void error(String msg){
        _log.severe(ANSI_RED + ANSI_BOLD + msg + ANSI_RESET);
    }

    public void setLogLevel(Level level){
        _log.setLevel(level);
    }
}
