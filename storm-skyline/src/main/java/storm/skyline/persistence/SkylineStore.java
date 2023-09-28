package storm.skyline.persistence;

import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import storm.skyline.model.repo.StormSkyTupleRepo;

public class SkylineStore {

    private static Optional<ApplicationContext> context = Optional.empty();

    public synchronized static StormSkyTupleRepo getStormSkyTupleRepo() {
        if (!context.isPresent()) {
            context = Optional.of(new ClassPathXmlApplicationContext("config/jpa-h2.xml"));
        }
        return (StormSkyTupleRepo) context.get().getBean("stormSkyTupleRepo");
    }

    public static void close() {
        ((ClassPathXmlApplicationContext) context.get()).close();
    }

}