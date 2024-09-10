package be.jimsa.reddoctor.config.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static be.jimsa.reddoctor.utility.constant.ProjectConstants.ANONYMOUS_USER;

@Component
@EnableJpaAuditing
public class AuditAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of(ANONYMOUS_USER);
    }
}
