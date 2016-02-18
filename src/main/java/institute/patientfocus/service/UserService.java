package institute.patientfocus.service;

import com.couchbase.client.protocol.views.ComplexKey;
import com.couchbase.client.protocol.views.Query;
import institute.patientfocus.domain.Authority;
import institute.patientfocus.domain.User;
import institute.patientfocus.repository.AuthorityRepository;
import institute.patientfocus.repository.UserRepository;
import institute.patientfocus.security.SecurityUtils;
import institute.patientfocus.service.util.RandomUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service class for managing users.
 */
@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);

    @Inject
    private PasswordEncoder passwordEncoder;

    @Inject
    private UserRepository userRepository;

    @Inject
    private SequenceService sequenceService;

    @Inject
    private AuthorityRepository authorityRepository;

    public Optional<User> activateRegistration(String key) {
        log.debug("Activating user for activation key {}", key);
        Query query = new Query();
        query.setKey(ComplexKey.of(key));
        query.setInclusiveEnd(true);
        Optional<User> activateUser = userRepository.findOneByActivationKey(query)
            .stream()
            .findFirst()
            .map(user -> {
                // activate given user for the registration key.
                user.setActivated(true);
                user.setActivationKey(null);
                userRepository.save(user);
                log.debug("Activated user: {}", user);
                return user;
            });
        if (activateUser.isPresent()) {
            return activateUser;
        }
        return Optional.empty();
    }

    public Optional<User> completePasswordReset(String newPassword, String key) {
        log.debug("Reset user password for reset key {}", key);
        Query query = new Query();
        query.setKey(key);
        query.setInclusiveEnd(true);
        return userRepository.findOneByResetKey(query)
            .stream()
            .findFirst()
            .filter(user -> {
                DateTime oneDayAgo = DateTime.now().minusHours(24);
                return user.getResetDate().isAfter(oneDayAgo.toInstant().getMillis());
            })
            .map(user -> {
                user.setPassword(passwordEncoder.encode(newPassword));
                user.setResetKey(null);
                user.setResetDate(null);
                userRepository.save(user);
                return user;
            });
    }

    public Optional<User> requestPasswordReset(String mail) {
        Query query = new Query();
        query.setKey(mail);
        query.setInclusiveEnd(true);
        return userRepository.findOneByEmail(query)
            .stream()
            .findFirst()
            .filter(user -> user.getActivated() == true)
            .map(user -> {
                user.setResetKey(RandomUtil.generateResetKey());
                user.setResetDate(DateTime.now());
                userRepository.save(user);
                return user;
            });
    }

    public User createUserInformation(String login, String password, String firstName, String lastName, String email,
                                      String langKey) {

        User newUser = new User();
        newUser.setId(sequenceService.nextId(User.SEQ));
        Authority authority = authorityRepository.findOne("ROLE_USER");
        List<String> authorities = new ArrayList<>();
        String encryptedPassword = passwordEncoder.encode(password);
        newUser.setLogin(login);
        // new user gets initially a generated password
        newUser.setPassword(encryptedPassword);
        newUser.setFirstName(firstName);
        newUser.setLastName(lastName);
        newUser.setEmail(email);
        newUser.setLangKey(langKey);
        // new user is not active
        newUser.setActivated(false);
        // new user gets registration key
        newUser.setActivationKey(RandomUtil.generateActivationKey());
        authorities.add(authority.getName());
        newUser.setAuthorities(authorities);
        userRepository.save(newUser);
        log.debug("Created Information for User: {}", newUser);
        return newUser;
    }

    public void updateUserInformation(String firstName, String lastName, String email, String langKey) {
        Query query = new Query();
        query.setKey(SecurityUtils.getCurrentLogin());
        query.setInclusiveEnd(true);
        userRepository.findOneByLogin(query)
            .stream()
            .findFirst()
            .ifPresent(u -> {
                u.setFirstName(firstName);
                u.setLastName(lastName);
                u.setEmail(email);
                u.setLangKey(langKey);
                userRepository.save(u);
                log.debug("Changed Information for User: {}", u);
            });
    }

    public void changePassword(String password) {
        Query query = new Query();
        query.setKey(SecurityUtils.getCurrentLogin());
        query.setInclusiveEnd(true);
        userRepository.findOneByLogin(query)
            .stream()
            .findFirst()
            .ifPresent(u -> {
                String encryptedPassword = passwordEncoder.encode(password);
                u.setPassword(encryptedPassword);
                userRepository.save(u);
                log.debug("Changed password for User: {}", u);
            });
    }

    public User getUserWithAuthorities() {
        Query query = new Query();
        query.setKey(SecurityUtils.getCurrentLogin());
        query.setInclusiveEnd(true);
        User currentUser = userRepository.findOneByLogin(query).get(0);
        currentUser.getAuthorities().size(); // eagerly load the association
        return currentUser;
    }

    /**
     * Not activated users should be automatically deleted after 3 days.
     * <p/>
     * <p>
     * This is scheduled to get fired everyday, at 01:00 (am).
     * </p>
     */
    @Scheduled(cron = "0 0 1 * * ?")
    public void removeNotActivatedUsers() {
        DateTime now = new DateTime();
        List<User> users = userRepository.findAllByActivatedIsFalseAndCreatedDateBefore(now.minusDays(3));
        for (User user : users) {
            log.debug("Deleting not activated user {}", user.getLogin());
            userRepository.delete(user);
        }
    }
}
