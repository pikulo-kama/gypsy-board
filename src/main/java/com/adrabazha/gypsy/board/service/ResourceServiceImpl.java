package com.adrabazha.gypsy.board.service;

import com.adrabazha.gypsy.board.utils.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ResourceServiceImpl implements ResourceService {

    private static final String USER_IMAGES_DIRECTORY_PATH = "static/images/defaultAvatars";
    private final FileUtils fileUtils;

    @Autowired
    public ResourceServiceImpl(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }

    @Override
    public String getRandomImage() {
        List<String> images = fileUtils.getFileNamesFromDirectory(USER_IMAGES_DIRECTORY_PATH);
        return images.get(new Random().nextInt(images.size()));
    }
}
