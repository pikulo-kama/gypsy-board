package com.adrabazha.gypsy.board.utils;

import com.adrabazha.gypsy.board.exception.GeneralException;
import org.springframework.stereotype.Component;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

@Component
public class FileUtils {

    public List<String> getFileNamesFromDirectory(String directory) {
        File directoryFile = getDirectory(directory)
                .orElseThrow(() -> new GeneralException("Directory not found"));

        return Arrays.stream(Objects.requireNonNull(directoryFile.listFiles()))
                .map(File::getName)
                .collect(Collectors.toList());
    }

    public Optional<File> getDirectory(String directory) {
        Optional<File> directoryFile = Optional.empty();
        URL resource = getClass().getClassLoader().getResource(directory);

        if (nonNull(resource)) {
            try {
                directoryFile = Optional.of(new File(resource.toURI()));
            } catch (URISyntaxException exception) {
                directoryFile = Optional.of(new File(resource.getPath()));
            }
        }
        return directoryFile;
    }
}
