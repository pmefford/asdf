package institute.patientfocus.config;

import com.couchbase.client.CouchbaseClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

import java.util.Arrays;
import java.util.List;

/**
 * Created by pmefford on 7/13/15.
 */
@Configuration
@EnableCouchbaseRepositories("institute.patientfocus.repository")
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {
    private final static Logger log = LoggerFactory.getLogger(CouchbaseConfig.class);

    @Value("${couchbase.cluster.bucket}")
    private String bucketName;

    @Value("${couchbase.cluster.password}")
    private String password;

    @Value("${couchbase.cluster.ip}")
    private String ip;

    @Override
    protected List<String> bootstrapHosts() {
        return Arrays.asList(ip);
    }

    @Override
    protected String getBucketName() {
        return bucketName;
    }

    @Override
    protected String getBucketPassword() {
        return password;
    }

    @Bean
    CommandLineRunner commandLineRunner(CouchbaseClient couchbaseClient) {
        return args -> {
            couchbaseClient.add("aKey", "{'json':'object'}");
            Object aKey = couchbaseClient().get("aKey");
            log.info("" + aKey);
        };
    }
}
