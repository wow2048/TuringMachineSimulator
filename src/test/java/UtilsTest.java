import edu.nju.Utils;
import org.junit.Test;

public class UtilsTest {
    @Test
    public void testSplitString() {
        String[] strings = Utils.SplitString("#Q = {0}");
        assert strings != null;
        for (String each : strings) {
            System.out.println(each);
        }
    }

}
