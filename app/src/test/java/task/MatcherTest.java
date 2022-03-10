package task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class MatcherTest {
    @Test
    public void testMatch() {
        HashSet<String> names = new HashSet<String>();
        names.add("James");
        names.add("Robert");

        Matcher matcher = new Matcher(names);
        // negative test
        {
            assertEquals(0, matcher.match("").size());
            assertThrows(InvalidParameterException.class, () -> {
                matcher.match(null);
            });
        }

        // basic test
        {
            Map<String, ArrayList<SourceLocation>> res = matcher.match("While Robert sleeps,\n James works");

            assertEquals(2, res.size());
            assertEquals(1, res.get("James").size());
            assertEquals(1, res.get("Robert").size());

            SourceLocation james = res.get("James").get(0);
            SourceLocation robert = res.get("Robert").get(0);

            assertEquals(6, robert.getChar());
            assertEquals(0, robert.getLine());

            assertEquals(22, james.getChar());
            assertEquals(1, james.getLine());
        }

        // start with name
        {
            Map<String, ArrayList<SourceLocation>> res = matcher.match("James has a kid");

            assertEquals(1, res.size());
            assertEquals(1, res.get("James").size());

            SourceLocation james = res.get("James").get(0);

            assertEquals(0, james.getChar());
            assertEquals(0, james.getLine());
        }

        // end with a name
        {
            Map<String, ArrayList<SourceLocation>> res = matcher.match("His name is   Robert");

            assertEquals(1, res.size());
            assertEquals(1, res.get("Robert").size());

            SourceLocation robert = res.get("Robert").get(0);

            assertEquals(14, robert.getChar());
            assertEquals(0, robert.getLine());
        }
    }
}
