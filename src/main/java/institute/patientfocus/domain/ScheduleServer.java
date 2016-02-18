package institute.patientfocus.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;
import org.springframework.data.couchbase.core.mapping.Field;

import java.util.Date;
import java.util.Objects;

/**
 * Created by pmefford on 8/30/15.
 */
@Document
public class ScheduleServer {
    @Id
    private final String id = "schedule-server";

    @Field
    private String serverName;

    @Field
    private String serverIp;

    @Field
    private Date updated;

    public String getId() {
        return id;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serverName, serverIp, updated);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final ScheduleServer other = (ScheduleServer) obj;
        return Objects.equals(this.id, other.id)
            && Objects.equals(this.serverName, other.serverName)
            && Objects.equals(this.serverIp, other.serverIp)
            && Objects.equals(this.updated, other.updated);
    }
}
