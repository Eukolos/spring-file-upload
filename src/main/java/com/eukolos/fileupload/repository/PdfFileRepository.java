package com.eukolos.fileupload.repository;

import com.eukolos.fileupload.model.PdfFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PdfFileRepository extends JpaRepository<PdfFile, Long> {

    Optional<PdfFile> findByName(String name);
}