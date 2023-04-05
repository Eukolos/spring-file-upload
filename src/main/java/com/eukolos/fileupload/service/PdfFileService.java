package com.eukolos.fileupload.service;

import com.eukolos.fileupload.model.PdfFile;
import com.eukolos.fileupload.repository.PdfFileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
public class PdfFileService {
    private final PdfFileRepository pdfFileRepository;
    private static String uploadDirectory;

    public PdfFileService(PdfFileRepository pdfFileRepository) {
        this.pdfFileRepository = pdfFileRepository;
    }
    @Value("${pdf.upload.directory}")
    public void setWeatherStackApiBaseUrl(String uploadDirectory) {
        PdfFileService.uploadDirectory = uploadDirectory;
    }

    public void uploadPdfFileToDb(MultipartFile file) throws IOException {
        PdfFile pdfFile = new PdfFile();
        pdfFile.setName(file.getOriginalFilename());
        pdfFile.setContent(file.getBytes());
        pdfFileRepository.save(pdfFile);
    }

    public void uploadPdfFileToLocal(MultipartFile file) throws IOException {
        PdfFile pdfFile = new PdfFile();
        pdfFile.setName(file.getOriginalFilename());
        pdfFile.setContent(file.getBytes());
        Path filePath = Paths.get(uploadDirectory, file.getOriginalFilename());
        Files.write(filePath, file.getBytes());
    }

    public ResponseEntity<byte[]> downloadPdfFileFromDb(String name) {
        Optional<PdfFile> pdfFileOptional = pdfFileRepository.findByName(name);
        if (pdfFileOptional.isPresent()) {
            PdfFile pdfFile = pdfFileOptional.get();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", name);
            headers.setContentLength(pdfFile.getContent().length);
            return new ResponseEntity<>(pdfFile.getContent(), headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<byte[]> downloadPdfFileFromLocal(String name) throws IOException {
        Path filePath = Paths.get(uploadDirectory, name);
        if (Files.exists(filePath)) {
            byte[] fileBytes = Files.readAllBytes(filePath);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("attachment", name);
            headers.setContentLength(fileBytes.length);
            return new ResponseEntity<>(fileBytes, headers, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
