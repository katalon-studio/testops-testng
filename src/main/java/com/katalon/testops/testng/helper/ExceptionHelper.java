package com.katalon.testops.testng.helper;

import com.katalon.testops.commons.helper.LogHelper;
import org.slf4j.Logger;

public class ExceptionHelper {

    private static final Logger logger = LogHelper.getLogger();

    public static <T> T wrap(Exception e) {
        logger.info("Rethrow exception: " + e.getClass().getName() + " " + e.getMessage());
        throw new IllegalStateException(e);
    }
}
