package org.example;

import org.example.visitor.ClassPrinter;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
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

        try (JarFile sampleJar = new JarFile(jarPath)) {
            Enumeration<JarEntry> enumeration = sampleJar.entries();

            while (enumeration.hasMoreElements()) {
                JarEntry entry = enumeration.nextElement();
                if (entry.getName().endsWith(".class")) {
                    ClassPrinter classPrinter = new ClassPrinter();
                    ClassReader classReader = new ClassReader(sampleJar.getInputStream(entry));
                    classReader.accept(classPrinter, 0);
                }
            }
        }
    }
}
