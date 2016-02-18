package institute.patientfocus.service;

import com.google.common.collect.Lists;
import institute.patientfocus.domain.ScheduleServer;
import institute.patientfocus.repository.ScheduleServerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Optional;

/**
 * Created by pmefford on 8/30/15.
 */
@Service
public class ScheduleServerService {
    @Autowired
    private ScheduleServerRepository repository;

    private static final Long oneHour = 60 * 60 * 1000L;

    public boolean isServerScheduled() throws UnknownHostException {
        Optional<ScheduleServer> server = Lists.newArrayList(repository.findAll()).stream().findFirst();
        boolean output = false;

        InetAddress address = InetAddress.getLocalHost();
        String hostname = address.getHostName();
        String ipAddress = address.getHostAddress();

        if (!server.isPresent()) {
            ScheduleServer server1 = new ScheduleServer();
            server1.setUpdated(new Date());
            server1.setServerIp(ipAddress);
            server1.setServerName(hostname);
            repository.save(server1);
            return true;
        } else {
            //This server was running the job previously
            if (server.get().getServerName().equals(hostname) && server.get().getServerIp().equals(ipAddress)) {
                server.get().setUpdated(new Date());
                repository.save(server.get());
                return true;
            }
            // current server has missed schedule by over 10 minutes
            if (server.get().getUpdated().getTime() < System.currentTimeMillis() - oneHour) {
                server.get().setServerName(hostname);
                server.get().setServerIp(ipAddress);
                server.get().setUpdated(new Date());
                repository.save(server.get());
                return true;
            }
        }
        return output;
    }
}
