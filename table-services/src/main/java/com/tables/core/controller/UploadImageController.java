package com.tables.core.controller;

import com.tables.core.service.UploadImageService;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.*;
import io.micronaut.http.multipart.CompletedFileUpload;
import io.micronaut.http.server.types.files.StreamedFile;
import jakarta.inject.Inject;

import java.util.Map;

@Controller("/api/images")
public class UploadImageController {
    @Inject
    private UploadImageService uploadImageService;

    @Post(uri = "/upload", consumes = MediaType.MULTIPART_FORM_DATA, produces = MediaType.APPLICATION_JSON)
//    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<Map<String, String>> upload(@Part("file") CompletedFileUpload file) {
        return HttpResponse.created(uploadImageService.upload(file));
    }

    @Get("/{filename}")
//    @Secured(SecurityRule.IS_ANONYMOUS)
    public HttpResponse<StreamedFile> serve(@PathVariable String filename) {
        return HttpResponse.ok(uploadImageService.serve(filename));
    }
}
