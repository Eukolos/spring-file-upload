package com.eukolos.fileupload.controller;

import com.eukolos.fileupload.service.PdfFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class PdfFileController {

    private final PdfFileService pdfFileService;

    public PdfFileController(PdfFileService pdfFileService) {
        this.pdfFileService = pdfFileService;
    }

    @PostMapping("/db-upload")
    /*@Operation(summary = "Get client by client request body",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "ClientRequest body.",
                    content = @Content(mediaType = MediaType.IMAGE_PNG_VALUE,
                            schema = @Schema(format = "binary",type = "string")
                    ),
            description = "Get the ClientAPI response object with uniqueue personal identification number as " +
                    "part of the request body.",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "The client", content = @Content(mediaType =
                            MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ClientApi.class))),
                    @ApiResponse(responseCode = "400",
                            description = "Invalid personal identification number", content = @Content(mediaType =
                            MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(example = "Invalid personal identification number."))),
                    @ApiResponse(responseCode = "404",
                            description = "Client not found.", content = @Content(mediaType =
                            MediaType.TEXT_PLAIN_VALUE,
                            schema = @Schema(example = "Client not found."))),
                    @ApiResponse(responseCode = "500", description = "Severe system failure has occured!", content =
                    @Content(mediaType = MediaType.TEXT_PLAIN_VALUE, schema = @Schema(
                            example = "Severe system failure has occured!")))}))*/
    public ResponseEntity<String> uploadPdfFileToDb(@RequestParam("file") MultipartFile file) {
        try {
            pdfFileService.uploadPdfFileToDb(file);
            return ResponseEntity.ok().body("File uploaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }
    @PostMapping("/local-upload")
    public ResponseEntity<String> uploadPdfFileToLocal(@RequestParam("file") MultipartFile file) {
        try {
            pdfFileService.uploadPdfFileToLocal(file);
            return ResponseEntity.ok().body("File uploaded successfully");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file");
        }
    }

    @GetMapping("/db-download")
    public ResponseEntity<byte[]> downloadPdfFileFromDb(@RequestParam("name") String name) {
        return pdfFileService.downloadPdfFileFromDb(name);
    }
    @GetMapping("/local-download")
    public ResponseEntity<byte[]> downloadPdfFileFromLocal(@RequestParam("name") String name) throws IOException {
        return pdfFileService.downloadPdfFileFromLocal(name);
    }
}
