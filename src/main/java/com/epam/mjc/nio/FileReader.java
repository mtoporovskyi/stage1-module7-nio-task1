package com.epam.mjc.nio;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;

public class FileReader {
    public Profile getDataFromFile(File file) {
        StringBuilder content = new StringBuilder();
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r");
             FileChannel fileChannel = randomAccessFile.getChannel()) {
            ByteBuffer buffer = ByteBuffer.allocate(1024);

            while (fileChannel.read(buffer) > 0) {
                buffer.flip();
                while (buffer.hasRemaining()) {
                    content.append((char) buffer.get());
                }
                buffer.clear();
                fileChannel.read(buffer);
            }
        } catch (FileNotFoundException e) {
            System.out.println("File is not found: " + e);
        } catch (IOException e) {
            System.out.println("The problem while reading the file: " + e);
        }

        return this.generateProfile(this.parseDataFromFile(content));
    }

    private HashMap<String, String> parseDataFromFile(StringBuilder content) {
        HashMap<String, String> map = new HashMap<>();

        String[] keyValuePairs = content.toString().split("\n");
        for (String valuePair : keyValuePairs) {
            String[] keyValuePair = valuePair.split(":", 2);
            map.put(keyValuePair[0].trim(), keyValuePair[1].trim());
        }

        return map;
    }

    private Profile generateProfile(HashMap<String, String> map) {
        Profile profile = new Profile();
        profile.setName(map.get("Name"));
        profile.setAge(Integer.valueOf(map.get("Age")));
        profile.setEmail(map.get("Email"));
        profile.setPhone(Long.valueOf(map.get("Phone")));

        return profile;
    }
}
