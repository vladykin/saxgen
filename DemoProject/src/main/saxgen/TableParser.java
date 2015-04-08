package name.vladykin.saxgen.demo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author Alexey Vladykin
 */
public class TableParser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Usage: TableParser xmlFile");
            System.exit(1);
        }

        for (Person p : readPeopleList(new File(args[0]))) {
            System.out.println(p);
        }
    }

    public static List<Person> readPeopleList(File file) throws Exception {
        SAXParser parser = SAXParserFactory.newInstance().newSAXParser();
        PersonHandler handler = new PersonHandler();
        parser.parse(file, handler);
        return handler.people;
    }

    private static class PersonHandler extends DefaultHandler {

        private final List<Person> people;
        private StringBuilder buf;
///     SAXGEN_STATE

        public PersonHandler() {
            people = new ArrayList<Person>();
        }

        private void newPerson() {
            people.add(new Person());
        }

        private void setFirstName(String firstName) {
            if (!people.isEmpty()) {
                people.get(people.size() - 1).setFirstName(firstName.trim());
            }
        }

        private void setLastName(String lastName) {
            if (!people.isEmpty()) {
                people.get(people.size() - 1).setLastName(lastName.trim());
            }
        }

        private void capture() {
            if (buf == null) {
                buf = new StringBuilder();
            } else {
                buf.setLength(0);
            }
        }

        private String captured() {
            if (buf == null) {
                return null;
            } else {
                String result = buf.toString();
                buf = null;
                return result;
            }
        }

/// <table class=="people">
/// <tr> </tr> /* first row is header */
/// (
///     <tr>
///     (
///         <td class=="banner"> </td> /* yes, it's a banner */ |
///         <td> /* num */ { newPerson(); } </td>
///         <td> /* last name */ { capture(); } </td> { setLastName(captured()); }
///         <td> /* middle name */ </td>
///         <td> /* first name */ { capture(); } </td> { setFirstName(captured()); }
///     )
///     </tr>
/// )*
/// </table>

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attrs) throws SAXException {
///         SAXGEN_START_TAG{,}
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
///         SAXGEN_END_TAG{}
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            if (buf != null) {
                buf.append(ch, start, length);
            }
        }

    }
}
