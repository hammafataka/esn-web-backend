package dev.mfataka.esnzlin.utils;

import java.io.ByteArrayOutputStream;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import io.vavr.control.Try;

/**
 * @author HAMMA FATAKA (mfataka@monetplus.cz)
 * @project esn-web-backend
 * @date 20.08.2024 13:26
 */
public class ImageUtils {

    public static final int BITE_SIZE = 10 * 1024;

    public static byte[] compressImage(byte[] data) {
        final var deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        return Try.withResources(() -> new ByteArrayOutputStream(data.length))
                .of(outputStream -> {
                    final var tmp = new byte[BITE_SIZE];

                    while (!deflater.finished()) {
                        int size = deflater.deflate(tmp);
                        outputStream.write(tmp, 0, size);
                    }
                    return outputStream.toByteArray();
                })
                .get();

    }

    public static byte[] decompressImage(byte[] data) {
        final var inflater = new Inflater();
        inflater.setInput(data);
        return Try.withResources(() -> new ByteArrayOutputStream(data.length))
                .of(outputStream -> {
                    byte[] tmp = new byte[BITE_SIZE];
                    while (!inflater.finished()) {
                        int count = inflater.inflate(tmp);
                        outputStream.write(tmp, 0, count);
                    }
                    return outputStream.toByteArray();
                })
                .onFailure(Throwable::printStackTrace)
                .get();

    }

    public static String getMimeType(final String base64) {
        final var parts = base64.split(",");
        final var mimeTypePart = parts[0];
        // Example: "data:image/png;base64"
        return mimeTypePart.split(";")[0].split(":")[1]; // Returns "image/png"
    }

}