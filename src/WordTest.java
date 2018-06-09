import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

import static org.junit.Assert.assertTrue;

public class WordTest {
    @Rule
    public Timeout globalTimeout = Timeout.millis(5000);

    @Test
    public void testWord() {
        Word value = new Word('a');
        int length = value.toString().length();
        boolean p1 = false;
        if (length == 10)
            p1 = true;
        assertTrue("Word style is not right", p1);
    }

}