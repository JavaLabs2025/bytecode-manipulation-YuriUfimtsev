package org.example;

import org.example.metrics.MetricsContext;
import org.example.visitor.MetricsClassVisitor;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class Main {

    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Please, use format: java <input.jar> <output.json>");
            return;
        }

        var jarPath = args[0];
        var jsonPath = args[1];

        if (jarPath == null || jarPath.isBlank()) {
            System.err.println("Input JAR path shouldn't be empty.");
            return;
        }

        if (jsonPath == null || jsonPath.isBlank()) {
            System.err.println("Output JSON path shouldn't be empty.");
            return;
        }

        try {
            var jarFile = Path.of(jarPath);
            var jsonFile = Path.of(jsonPath);

            if (!Files.exists(jarFile) || !Files.isReadable(jarFile)) {
                System.err.println("Cannot read input JAR: " + jarPath);
                return;
            }

            System.out.println("Input:  " + jarFile);
            System.out.println("Output: " + jsonFile);
        } catch (InvalidPathException e) {
            System.err.println("Invalid file path:");
            System.err.println(e.getMessage());
            return;
        }

        var context = new MetricsContext();

        try (JarFile sampleJar = new JarFile(jarPath)) {
            Enumeration<JarEntry> enumeration = sampleJar.entries();

            while (enumeration.hasMoreElements()) {
                JarEntry entry = enumeration.nextElement();

                if (!entry.getName().endsWith(".class")) {
                    continue;
                }

                try (InputStream input = sampleJar.getInputStream(entry)) {
                    var classReader = new ClassReader(input);
                    var classVisitor = new MetricsClassVisitor(context);
                    classReader.accept(classVisitor, 0);
                }
            }
        }

        System.out.println("Classes found: " +  context.classesInfo.size());
        for (var ci : context.classesInfo.values()) {
            System.out.println(ci.name + " extends " + ci.superName + ", fields=" + ci.fieldCount);
        }
    }
}
