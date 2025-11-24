package application.usecases;

import application.interfaces.GlobalConfigRepository;
import domain.common.DomainException;
import domain.configuration.GlobalConfig;
import domain.users.User;

public class UpdateGlobalConfig {

    private static final double DEFAULT_PROFIT_MARGIN = 0.50;

    private final GlobalConfigRepository repository;

    public UpdateGlobalConfig(GlobalConfigRepository repository) {
        this.repository = repository;
    }

    public void execute(User actor, double newMargin) {
        if (!actor.isAdmin()) {
            throw new DomainException("Access Denied: Only ADMIN can modify global configuration.");
        }

        GlobalConfig config = repository.find()
            .orElse(new GlobalConfig(DEFAULT_PROFIT_MARGIN));

        config.updateProfitMargin(newMargin);

        repository.save(config);
    }
}
