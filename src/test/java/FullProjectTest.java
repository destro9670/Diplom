import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        T1Database.class,
        T2Auth.class,
        T3AuthError.class,
})
public class FullProjectTest {
    public static void main(String[] args) {

    }
}
