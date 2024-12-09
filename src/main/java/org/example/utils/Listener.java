package org.example.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;

@Slf4j
public class Listener implements TestWatcher {
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        log.debug("Тест не прошел");
    }

    @Override
    public void testSuccessful(ExtensionContext context) {
        log.debug("Тест пройден");
    }

}
