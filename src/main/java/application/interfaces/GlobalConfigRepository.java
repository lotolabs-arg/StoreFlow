package application.interfaces;

import domain.configuration.GlobalConfig;
import java.util.Optional;

public interface GlobalConfigRepository {

    GlobalConfig save(GlobalConfig config);

    /**
     * Retrieves the single configuration record.
     *
     * @return an Optional containing the config if initialized
     */
    Optional<GlobalConfig> find();
}
