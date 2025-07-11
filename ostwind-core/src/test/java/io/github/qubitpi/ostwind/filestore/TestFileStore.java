/*
 * Copyright 2024 Jiaqi Liu
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
package io.github.qubitpi.ostwind.filestore;

import io.github.qubitpi.ostwind.file.File;
import io.github.qubitpi.ostwind.file.identifier.FileIdGenerator;

import io.github.qubitpi.ostwind.application.JerseyTestBinder;
import io.github.qubitpi.ostwind.application.TestBinderFactory;

import jakarta.validation.constraints.NotNull;
import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.NotThreadSafe;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import jakarta.inject.Inject;
import jakarta.inject.Named;

/**
 * A {@link FileStore} test stub that facilitates FileServletSpec mocking through {@link TestBinderFactory} and
 * {@link JerseyTestBinder}.
 */
@NotThreadSafe
public class TestFileStore implements FileStore {

    /**
     * There is a reason of not having {@code Map<String, File>}.
     * <p>
     * When using {@code Map<String, File>} FileServletSpec test fails, because there seems to be max size posted on
     * the Map value. Hence, the File.fileContent is truncated, which makes the truncated InputStream unreadable during
     * the file download operation later.
     * <p>
     * Even using {@code Map<String, String>}, the Map value still got truncated (e.g. a file of 14500+ lines of text
     * got truncated to about 14300+ lines), but this is okay because the string, during the file download process, can
     * still be converted to InputStream. The test just needs a little bit of awareness to use expected.contains(actual)
     * instead of expected == actual. The is the best workaround that can be achieved.
     * <p>
     * But in production environment, there is no such problem. For example, when an large InputStream gets uploaded
     * to OpenStack Swift, for example, the object storage gracefully handles such case.
     */
    @GuardedBy("this")
    private final Map<String, String> fileByFileId;

    @GuardedBy("this")
    private final FileIdGenerator fileIdGenerator;

    /**
     * Constructor.
     *
     * @param fileByFileId  A mapping that offers canned answer to calls made to {@link FileStore} during the test.
     * @param fileIdGenerator  A per-test def defined logic, which overrides the file ID generation
     */
    @Inject
    public TestFileStore(
            final @NotNull @Named("fileByFileId") Map<String, String> fileByFileId,
            final @NotNull @Named("fileIdGenerator") FileIdGenerator fileIdGenerator
    ) {
        this.fileByFileId = Objects.requireNonNull(fileByFileId);
        this.fileIdGenerator = Objects.requireNonNull(fileIdGenerator);
    }

    @Override
    public String upload(final File file) {
        final String fileId = fileIdGenerator.apply(file);
        fileByFileId.put(
                fileIdGenerator.apply(file),
                new BufferedReader(new InputStreamReader(file.getFileContent()))
                        .lines().collect(Collectors.joining("\n"))
        );
        return fileId;
    }

    @Override
    public InputStream download(final String fileId) {
        return new ByteArrayInputStream(fileByFileId.get(fileId).getBytes());
    }
}
