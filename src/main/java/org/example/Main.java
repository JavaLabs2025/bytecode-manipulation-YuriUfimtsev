package org.example;

import org.example.metrics.MetricsCalculator;
import org.example.metrics.MetricsContext;
import org.example.metrics.MetricsResult;
import org.example.visitor.MetricsClassVisitor;
import org.objectweb.asm.ClassReader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

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

        var metricsResult = getMetricsResult(jarPath);
        System.out.println(metricsResult.toString());

        var mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        var jsonFile = new File(jsonPath);
        mapper.writeValue(jsonFile, metricsResult);

//        System.out.println("Classes found: " +  context.getAllClassesInfo().size());
//        for (var ci : context.getAllClassesInfo()) {
//            System.out.println(
//                    ci.getName() + " extends " + ci.getSuperName() +", fields=" + ci.getFieldCount()
//            );
//        }
    }

    private static MetricsResult getMetricsResult(String jarPath) throws IOException {
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

        var metricsCalculator = new MetricsCalculator(context);
        var metricsResult = metricsCalculator.calculate();
        return metricsResult;
    }
}
