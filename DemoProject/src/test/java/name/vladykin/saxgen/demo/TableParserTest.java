package name.vladykin.saxgen.demo;

import org.junit.Test;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class TableParserTest {

    @Test
    public void testName() throws Exception {
        File testData = new File(TableParserTest.class.getResource("/testdata.xhtml").toURI());
        List<Person> people = TableParser.readPeopleList(testData);

        assertEquals(4, people.size());
        assertEquals("Ivan", people.get(0).getFirstName());
        assertEquals("Ivanov", people.get(0).getLastName());
        assertEquals("Petr", people.get(1).getFirstName());
        assertEquals("Petrov", people.get(1).getLastName());
        assertEquals("Sidor", people.get(2).getFirstName());
        assertEquals("Sidorov", people.get(2).getLastName());
        assertEquals("Trofim", people.get(3).getFirstName());
        assertEquals("Trofimov", people.get(3).getLastName());
    }
}
