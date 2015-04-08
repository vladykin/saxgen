package name.vladykin.saxgen;

import name.vladykin.saxgen.state.TransitionTable;
import name.vladykin.saxgen.state.StateGenerator;
import name.vladykin.saxgen.model.EndTag;
import name.vladykin.saxgen.model.Expr;
import name.vladykin.saxgen.model.StartTag;
import name.vladykin.saxgen.model.InputElement;
import name.vladykin.saxgen.model.AttrExpr;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import name.vladykin.saxgen.input.InputParser;
import name.vladykin.saxgen.input.ParseException;
import name.vladykin.saxgen.model.Tag;

/**
 * @author Alexey Vladykin
 */
public class SaxGen {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: SaxGen srcFile dstFile");
            System.exit(1);
        }

        String srcFile = args[0];
        String dstFile = args[1];
        System.err.println("Converting " + srcFile + " to " + dstFile);
        try {
            new SaxGen(new File(srcFile), new File(dstFile)).transform();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
    }

    private final File oldFile;
    private final File newFile;
    private boolean stateWritten;
    private List<String> startTagHandler;
    private boolean startTagWritten;
    private List<String> endTagHandler;
    private boolean endTagWritten;

    private SaxGen(File oldFile, File newFile) {
        this.oldFile = oldFile;
        this.newFile = newFile;
    }

    private void transform() throws IOException, ParseException {
        String oldFilename = oldFile.getName();
        String oldClassname = oldFilename.substring(0, oldFilename.length() - FILE_EXT.length());
        String newFilename = newFile.getName();
        String newClassname = newFilename.substring(0, newFilename.length() - FILE_EXT.length());
        BufferedReader reader = new BufferedReader(new FileReader(oldFile));
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(newFile));
            try {
                transform(reader, writer, oldClassname, newClassname);
            } finally {
                writer.close();
            }
        } finally {
            reader.close();
        }
    }
    private static final String FILE_EXT = ".java";
    private static final String LINE_PREFIX = "///";
    private static final Pattern STATE_PATTERN = Pattern.compile("^(///\\s+)SAXGEN_STATE\\b");
    private static final Pattern START_TAG_PATTERN = Pattern.compile("^(///\\s+)SAXGEN_START_TAG\\{\\s*(.*?),\\s*(.*?)\\s*\\}");
    private static final Pattern END_TAG_PATTERN = Pattern.compile("^(///\\s+)SAXGEN_END_TAG\\{\\s*(.*?)\\s*\\}");

    private void writeIndent(BufferedWriter w, int count) throws IOException {
        for (int i = 0; i < count; ++i) {
            w.write(' ');
        }
    }

    private void writeComment(BufferedWriter w, int indent, String text) throws IOException {
        w.write("//");
        writeIndent(w, indent - 2);
        w.write(text);
    }

    private void transform(BufferedReader r, BufferedWriter w, String oldClassname, String newClassname) throws IOException, ParseException {
        String line;
        while ((line = r.readLine()) != null) {
            if (line.startsWith(LINE_PREFIX)) {
                Matcher stateMatcher = STATE_PATTERN.matcher(line);
                if (stateMatcher.matches()) {
                    writeState(w, stateMatcher.group(1).length());
                    continue;
                }

                Matcher startTagMatcher = START_TAG_PATTERN.matcher(line);
                if (startTagMatcher.matches()) {
                    writeStartTagHandler(w, startTagMatcher.group(1).length(), startTagMatcher.group(2), startTagMatcher.group(3));
                    continue;
                }

                Matcher endTagMatcher = END_TAG_PATTERN.matcher(line);
                if (endTagMatcher.matches()) {
                    writeEndTagHandler(w, endTagMatcher.group(1).length(), endTagMatcher.group(2));
                    continue;
                }

                StringBuilder buf = new StringBuilder();
                do {
                    buf.append(line.substring(3)).append('\n');
                    line = r.readLine();
                } while (line != null && line.startsWith(LINE_PREFIX));
                parseGrammar(buf.toString());
            }
            if (line != null) {
                w.write(line.replaceAll(oldClassname, newClassname));
                w.write('\n');
            }
        }
    }

    private void writeState(BufferedWriter w, int indent) throws IOException {
        if (stateWritten) {
            writeComment(w, indent, "SaxGen state already written. Ignoring.");
        } else {
            writeIndent(w, indent);
            w.write("private int state = 0;\n");
            stateWritten = true;
        }
    }

    private void writeStartTagHandler(BufferedWriter w, int indent, String tagNamePattern, String tagAttributePattern) throws IOException {
        if (startTagWritten) {
            writeComment(w, indent, "SaxGen startTagHandler already written. Ignoring.");
        } else {
            if (startTagHandler == null) {
                writeComment(w, indent, "SaxGen startTagHandler can't precede grammar definition. Ignoring.");
            } else {
                for (String line : startTagHandler) {
                    writeIndent(w, indent);
                    w.write(line); // TODO: use tagNamePattern and tagAttributePattern
                    w.write('\n');
                }
            }
        }
    }

    private void writeEndTagHandler(BufferedWriter w, int indent, String tagNamePattern) throws IOException {
        if (endTagWritten) {
            writeComment(w, indent, "SaxGen endTagHandler already written. Ignoring.");
        } else {
            if (endTagHandler == null) {
                writeComment(w, indent, "SaxGen endTagHandler can't precede grammar definition. Ignoring.");
            } else {
                for (String line : endTagHandler) {
                    writeIndent(w, indent);
                    w.write(line); // TODO: use tagNamePattern
                    w.write('\n');
                }
            }
        }
    }

    private void parseGrammar(String input) throws ParseException {
        InputParser parser = new InputParser(new StringReader(input));
        List<InputElement> list = parser.parse();
        TransitionTable table = StateGenerator.generate(list);
        renderStartTagHandler(table);
        renderEndTagHandler(table);
    }


    private void renderStartTagHandler(TransitionTable states) {
        startTagHandler = new ArrayList<String>();
        startTagHandler.add("switch (state) {");
        for (int i = 0; i < states.size(); ++i) {
            List<Map.Entry<Tag, Integer>> list = extractStartTags(states.getTransitions(i));
            if (!list.isEmpty()) {
                startTagHandler.add("    case " + i + ":");
                for (Iterator<Map.Entry<Tag, Integer>> it = list.iterator(); it.hasNext();) {
                    Map.Entry<Tag, Integer> entry = it.next();
                    StartTag startTag = (StartTag) entry.getKey();
                    startTagHandler.add("        if (" + renderCondition(startTag) + ") {");
                    if (startTag.getCode() != null) {
                        startTagHandler.add("            " + insertAttrs(startTag.getCode().trim()));
                    }
                    startTagHandler.add("            state = " + entry.getValue() + ';');
                    if (it.hasNext()) {
                        startTagHandler.add("        } else");
                    } else {
                        startTagHandler.add("        }");
                    }
                }
                startTagHandler.add("        break;");
            }
        }
        startTagHandler.add("}");
    }

    private List<Map.Entry<Tag, Integer>> extractStartTags(Map<Tag, Integer> map) {
        List<Map.Entry<Tag, Integer>> list = new ArrayList<Map.Entry<Tag, Integer>>();
        for (Map.Entry<Tag, Integer> entry : map.entrySet()) {
            if (entry.getKey() instanceof StartTag) {
                list.add(entry);
            }
        }
        return list;
    }

    private List<Map.Entry<Tag, Integer>> extractEndTags(Map<Tag, Integer> map) {
        List<Map.Entry<Tag, Integer>> list = new ArrayList<Map.Entry<Tag, Integer>>();
        for (Map.Entry<Tag, Integer> entry : map.entrySet()) {
            if (entry.getKey() instanceof EndTag) {
                list.add(entry);
            }
        }
        return list;
    }

    private void renderEndTagHandler(TransitionTable states) {
        endTagHandler = new ArrayList<String>();
        endTagHandler.add("switch (state) {");
        for (int i = 0; i < states.size(); ++i) {
            List<Map.Entry<Tag, Integer>> list = extractEndTags(states.getTransitions(i));
            if (!list.isEmpty()) {
                endTagHandler.add("    case " + i + ":");
                for (Iterator<Map.Entry<Tag, Integer>> it = list.iterator(); it.hasNext();) {
                    Map.Entry<Tag, Integer> entry = it.next();
                    EndTag endTag = (EndTag) entry.getKey();
                    endTagHandler.add("        if (" + renderCondition(endTag) + ") {");
                    if (endTag.getCode() != null) {
                        endTagHandler.add("            " + endTag.getCode().trim());
                    }
                    endTagHandler.add("            state = " + entry.getValue() + ';');
                    if (it.hasNext()) {
                        endTagHandler.add("        } else");
                    } else {
                        endTagHandler.add("        }");
                    }
                }
                endTagHandler.add("        break;");
            }
        }
        endTagHandler.add("}");
    }

    private String renderCondition(InputElement elem) {
        StringBuilder buf = new StringBuilder();
        if (elem instanceof StartTag) {
            StartTag startTag = (StartTag) elem;
            buf.append('"').append(startTag.getName()).append("\".equals(qName)");
            if (startTag.getCondition() != null) {
                buf.append(" && ");
                renderCondition(startTag.getCondition(), buf);
            }
        } else if (elem instanceof EndTag) {
            buf.append('"').append(((EndTag) elem).getName()).append("\".equals(qName)");
        } else {
            throw new IllegalArgumentException();
        }
        return buf.toString();
    }

    private static void renderCondition(Expr expr, StringBuilder buf) {
        if (expr instanceof AttrExpr) {
            renderCondition((AttrExpr) expr, buf);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private static void renderCondition(AttrExpr expr, StringBuilder buf) {
        switch (expr.getOperator()) {
            case EQUALS:
                if (expr.getValue() == null) {
                    buf.append("null == attrs.getValue(\"").append(expr.getAttribute()).append("\")");
                } else {
                    buf.append("\"").append(expr.getValue()).append("\".equals(attrs.getValue(\"").append(expr.getAttribute()).append("\"))");
                }
                break;
            case NOT_EQUALS:
                if (expr.getValue() == null) {
                    buf.append("null != attrs.getValue(\"").append(expr.getAttribute()).append("\")");
                } else {
                    buf.append("!\"").append(expr.getValue()).append("\".equals(attrs.getValue(\"").append(expr.getAttribute()).append("\"))");
                }
                break;
            case SUBSTR_EQUALS:
                buf.append("null != attrs.getValue(\"").append(expr.getAttribute()).append("\") && ");
                buf.append("0 <= attrs.getValue(\"").append(expr.getAttribute()).append("\").indexOf(\"").append(expr.getValue()).append("\")");
                break;
            case NOT_SUBSTR_EQUALS:
                buf.append("(null == attrs.getValue(\"").append(expr.getAttribute()).append("\") || ");
                buf.append("-1 == attrs.getValue(\"").append(expr.getAttribute()).append("\").indexOf(\"").append(expr.getValue()).append("\"))");
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private static CharSequence insertAttrs(CharSequence cs) {
        return Pattern.compile("@([a-zA-Z]+)").matcher(cs).replaceAll("attrs.getValue(\"$1\")");
    }

}
