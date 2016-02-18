package institute.patientfocus.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.couchbase.client.protocol.views.Query;
import institute.patientfocus.domain.Authority;
import institute.patientfocus.domain.User;
import institute.patientfocus.repository.AuthorityRepository;
import institute.patientfocus.repository.UserRepository;
import institute.patientfocus.security.AuthoritiesConstants;
import institute.patientfocus.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pmefford on 7/18/15.
 */
@RestController
@RequestMapping("/api")
public class StartupResource {


    private final Logger log = LoggerFactory.getLogger(UserResource.class);

    @Inject
    private UserRepository userRepository;

    @Inject
    private UserService userService;

    @Inject
    private AuthorityRepository authorityRepository;


    @RequestMapping(value = "/setup/setup3",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<List<User>> setRoles(@RequestParam("password") String password) {
        log.debug("REST request to get setup : {}", password);
        if (!"now123".equals(password)) {
            return new ResponseEntity("failed", HttpStatus.FORBIDDEN);
        }

        Query querySystem = new Query();
        querySystem.setKey("system");
        querySystem.setInclusiveEnd(true);
        User system = userRepository.findOneByLogin(querySystem).stream().findFirst().get();
        system.setActivated(true);
        system.getAuthorities().add(AuthoritiesConstants.ADMIN);
        userRepository.save(system);
        List<User> userList = new ArrayList<>();


        Query queryAdmin = new Query();
        queryAdmin.setKey("admin");
        queryAdmin.setInclusiveEnd(true);
        User admin = userRepository.findOneByLogin(queryAdmin).stream().findFirst().get();
        admin.getAuthorities().add(AuthoritiesConstants.ADMIN);
        admin.setActivated(true);
        admin.setCreatedBy("system");
        userRepository.save(admin);


        Query queryAnony = new Query();
        queryAnony.setKey("anonymousUser");
        queryAnony.setInclusiveEnd(true);
        User anonymousUser = userRepository.findOneByLogin(queryAnony).stream().findFirst().get();
        anonymousUser.setAuthorities(new ArrayList<>());
        anonymousUser.getAuthorities().add(AuthoritiesConstants.ANONYMOUS);
        anonymousUser.setActivated(true);
        anonymousUser.setCreatedBy("system");
        userRepository.save(anonymousUser);


        Query queryUser = new Query();
        queryUser.setKey("user");
        queryUser.setInclusiveEnd(true);
        User user = userRepository.findOneByLogin(queryUser).stream().findFirst().get();
        user.setActivated(true);
        user.setCreatedBy("system");
        userRepository.save(user);


        userList.add(system);
        userList.add(admin);
        userList.add(anonymousUser);
        userList.add(user);

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @RequestMapping(value = "/setup/setup2",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<List<User>> setRolesAndAccounts(@RequestParam("password") String password) {
        log.debug("REST request to get setup : {}", password);
        if (!"now123".equals(password)) {
            return new ResponseEntity("failed", HttpStatus.FORBIDDEN);
        }
        userRepository.deleteAll();

        User system = userService.createUserInformation("system", "system", "", "System", "", "en");

        User admin = userService.createUserInformation("admin", "admin", "", "Administrator", "", "en");

        User anonymousUser = userService.createUserInformation("anonymousUser", "anonymousUser", "Anonymous", "User", "user@localhost", "en");

        User user = userService.createUserInformation("user", "user", "", "User", "", "en");

        List<User> userList = new ArrayList<>();

        userList.add(system);
        userList.add(admin);
        userList.add(anonymousUser);
        userList.add(user);

        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @RequestMapping(value = "/setup/setup1",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<String> setupRoles(@RequestParam("password") String password) {
        log.debug("REST request to get setup authorities: {}", password);
        if (!"now123".equals(password)) {
            return new ResponseEntity("failed", HttpStatus.FORBIDDEN);
        }
        Authority admin = new Authority();
        admin.setName("ROLE_ADMIN");
        authorityRepository.save(admin);
        Authority user = new Authority();
        user.setName("ROLE_USER");
        authorityRepository.save(user);
        return new ResponseEntity<>("completed", HttpStatus.OK);
    }


    @RequestMapping(value = "/setup/delete",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<String> deleteSetup(@RequestParam("password") String password) {
        log.debug("REST request to delete : {}", password);
        if (!"now123".equals(password)) {
            return new ResponseEntity("failed", HttpStatus.FORBIDDEN);
        }
        authorityRepository.deleteAll();

        return new ResponseEntity<>("completed", HttpStatus.OK);
    }


    @RequestMapping(value = "/setup/deleteUsers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    ResponseEntity<String> deleteUsers(@RequestParam("password") String password) {
        log.debug("REST request to delete users: {}", password);
        if (!"now123".equals(password)) {
            return new ResponseEntity("failed", HttpStatus.FORBIDDEN);
        }
        userRepository.deleteAll();

        return new ResponseEntity<>("completed", HttpStatus.OK);
    }

}
