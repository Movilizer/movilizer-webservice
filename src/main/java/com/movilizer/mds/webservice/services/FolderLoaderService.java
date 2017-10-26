/*
 * Copyright 2015 Movilizer GmbH
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.movilizer.mds.webservice.services;

import com.movilitas.movilizer.v15.MovilizerRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;


class FolderLoaderService {
    private static final Logger logger = LoggerFactory.getLogger(FolderLoaderService.class);

    private final MovilizerXMLParserService parserService;

    public FolderLoaderService(final MovilizerXMLParserService parserService) {
        this.parserService = parserService;
    }

    List<MovilizerRequest> loadRequestsFromFolder(Path folder) throws IOException {
        final List<MovilizerRequest> out = new ArrayList<>();

        Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path filePath, BasicFileAttributes attrs) throws IOException {
                if (filePath.toString().endsWith(".mxml")) {
                    MovilizerRequest request = parserService.getRequestFromFile(filePath);
                    out.add(request);
                    if (logger.isDebugEnabled()) {
                        logger.debug("Loaded file: " + filePath.getFileName());
                    }
                }
                return FileVisitResult.CONTINUE;
            }
        });

        return out;
    }

}
