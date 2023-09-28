package com.skyline;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.AppenderSkeleton;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SkylineRunnerTest {

    private TestAppender appender;

    @Before
    public void setup() {
        appender = new TestAppender();
        Logger.getRootLogger().addAppender(appender);
    }

    @Test
    public void testMain() {
        SkylineRunner.main(new String[0]);
        Assert.assertTrue(true);
        Assert.assertTrue(appender.getMessages().stream().anyMatch(m -> m.equals("Success")));
        Assert.assertTrue(appender.getMessages().stream().anyMatch(m -> m.contains("Skyline")));
    }

    @Test
    public void testBasicSkyline() throws IOException {
        SkylineRunner.basicSkyline("src/test/resources/structures/basic.json");
        Assert.assertTrue(true);
        Assert.assertTrue(appender.getMessages().stream().anyMatch(m -> m.equals("Success")));
        Assert.assertTrue(appender.getMessages().stream().anyMatch(m -> m.contains("Skyline")));
    }

    @Test
    public void testEventSkyline() throws IOException {
        SkylineRunner.eventSkyline("src/test/resources/structures/basic.json");
        Assert.assertTrue(true);
        Assert.assertTrue(appender.getMessages().stream().anyMatch(m -> m.equals("Success")));
        Assert.assertTrue(appender.getMessages().stream().anyMatch(m -> m.contains("Skyline")));
    }

    @Test
    public void testGenerateTestData() throws IOException {
        SkylineRunner.generateTestData("src/test/resources/structures/basic.json", "src/test/resources/tuples/generated.json");
        Assert.assertTrue(true);
        Assert.assertTrue(appender.getMessages().stream().anyMatch(m -> m.equals("Success")));
        Assert.assertTrue(appender.getMessages().stream().anyMatch(m -> m.contains("Finished")));
    }

    @After
    public void cleanup() {
        Logger.getRootLogger().removeAppender(appender);
    }

    public static class TestAppender extends AppenderSkeleton {

        private final List<String> messages = new ArrayList<>();

        @Override
        protected void append(LoggingEvent event) {
            messages.add(event.getRenderedMessage());
        }

        @Override
        public void close() {

        }

        @Override
        public boolean requiresLayout() {
            return false;
        }

        public List<String> getMessages() {
            return messages;
        }

    }

}
