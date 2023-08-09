package com.digte.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.IOUtils;

public class StreamUtil {

    public static final String SUFFIX = ".tmp";

    public static StreamingOutput stream2file (InputStream in, String filename) throws IOException {
        final File tempFile = File.createTempFile(filename, SUFFIX);
        tempFile.deleteOnExit();
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            IOUtils.copy(in, out);
        }

        final InputStream responseStream = new FileInputStream(tempFile);

        StreamingOutput output = new StreamingOutput() {
            @Override
            public void write(OutputStream out) throws IOException, WebApplicationException {
                int length;
                byte[] buffer = new byte[1024];
                while((length = responseStream.read(buffer)) != -1) {
                    out.write(buffer, 0, length);
                }
                out.flush();
                responseStream.close();
            }
        };

        return output;
    }

}