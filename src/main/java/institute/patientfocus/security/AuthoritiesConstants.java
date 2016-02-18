package institute.patientfocus.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    private AuthoritiesConstants() {
    }

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    //min role is for survey takes who are identified by email link or survey launch page
    public static final String MIN = "ROLE_MIN";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";
}
