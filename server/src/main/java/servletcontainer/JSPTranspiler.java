package servletcontainer;

import org.checkerframework.checker.units.qual.A;

import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.ToolProvider;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URI;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JSPTranspiler {
    final private JavaCompiler compiler;
    private List<String> definitions;
    private List<String> imports;

    public JSPTranspiler() {
        compiler = ToolProvider.getSystemJavaCompiler();
        definitions = new ArrayList<>();
        imports = new ArrayList<>();
    }

    public synchronized void compile(File appDir, File file, String servletUrl) {
        String javaSource = transpile(file, servletUrl);

        URI sourceUri = URI.create(file.getName().replace(".jsp", ".java"));
        JavaFileObject sourceFile = new JavaStringSource(sourceUri, javaSource);

        List<String> options = new ArrayList<>();
        options.add("-d");
        options.add(appDir.getAbsolutePath());
        options.add("-cp");

        StringBuilder classpaths = new StringBuilder();
        classpaths.append("server/src/main/java:");
        classpaths.append(appDir.getAbsolutePath() + ":");
        classpaths.append(appDir.getAbsolutePath() + ":");
        classpaths.append(Paths.get(appDir.getAbsolutePath(), "WEB-INF").toString() + ":");
        classpaths.append(Paths.get(appDir.getAbsolutePath(), "WEB-INF", "classes").toString() + ":");
        classpaths.append(Paths.get(appDir.getAbsolutePath(), "WEB-INF", "lib").toString() + ":");
        classpaths.append(Paths.get(appDir.getAbsolutePath(), "WEB-INF", "lib").toString());

        options.add(classpaths.toString());

        Boolean ok = compiler.getTask(null, null, null, options,
                null, Arrays.asList(sourceFile)).call();

        if (ok) {
            System.out.println(file.getName() + " compiled successfuly");
        } else {
            throw new RuntimeException("Compilation failed.");
        }
    }

//    String transpile(File file, String servlerUrl) {
//        String fileName = file.getName().replace(".jsp", "");
//        String code = "public class " + fileName + " {\n    public void start() {\n        System.out.println(\"Hello, world!\");\n    }\n}";
//        return code;
//    }

    private String transpile(File file, String servlerUrl) {
        clear();

        String fileName = file.getName().replace(".jsp", "");
        StringBuilder code = new StringBuilder();

        code.append("import java.io.IOException;\n");
        code.append("import servletcontainer.api.*;\n");
        code.append("@Servlet(url=\"" + servlerUrl + "\")\n");
        code.append("public class " + fileName + " implements HttpServlet {\n");
        code.append("""
            @Override
            public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
                try {
                    var out = resp.getOutputStream();
        """);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                code.append("out.println(\"" + line + "\");\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        code.append("""
                } catch (IOException e) {
                            e.printStackTrace();
                }
            }
        }
        """);

        return code.toString();
    }

    private void clear() {
        definitions.clear();
        imports.clear();

    }

    private static class JavaStringSource extends SimpleJavaFileObject {
        private final String code;

        public JavaStringSource(URI uri, String code) {
            super(uri, Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }
}
