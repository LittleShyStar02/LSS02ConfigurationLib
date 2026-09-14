package io.github.littleshystar02.lss02.config.resource;

import java.io.File;

public interface ResourceExtractor {

    File extract(String resourcePath, File targetFile);

}
