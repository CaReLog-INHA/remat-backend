package com.remat.domain.ai.service;

import com.remat.domain.ai.exception.AIException;
import com.remat.domain.ai.exception.enums.AIErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class PdfTextExtractService {

    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB
    private static final String PDF_CONTENT_TYPE = "application/pdf";

    public String extract(MultipartFile file) {
        validate(file);

        try {
            byte[] bytes = file.getBytes();
            return extractText(bytes);
        } catch (IOException e) {
            throw new AIException(AIErrorCode.PDF_PARSE_FAILED, e);
        }
    }

    public String extract(byte[] pdfBytes) {
        if (pdfBytes == null || pdfBytes.length == 0) {
            throw new AIException(AIErrorCode.EMPTY_FILE);
        }
        return extractText(pdfBytes);
    }

    private String extractText(byte[] pdfBytes) {
        try (PDDocument document = Loader.loadPDF(pdfBytes)) {
            PDFTextStripper stripper = new PDFTextStripper();
            String text = stripper.getText(document);

            if (text == null || text.isBlank()) {
                throw new AIException(AIErrorCode.PDF_EMPTY_CONTENT);
            }

            log.debug("PDF 텍스트 추출 완료: {}자", text.length());
            return text.trim();

        } catch (AIException e) {
            throw e;
        } catch (IOException e) {
            throw new AIException(AIErrorCode.PDF_PARSE_FAILED, e);
        }
    }

    private void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new AIException(AIErrorCode.EMPTY_FILE);
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new AIException(AIErrorCode.FILE_TOO_LARGE);
        }

        String contentType = file.getContentType();
        String originalFilename = file.getOriginalFilename();

        boolean isValidContentType = PDF_CONTENT_TYPE.equals(contentType);
        boolean isValidExtension = originalFilename != null
                && originalFilename.toLowerCase().endsWith(".pdf");

        if (!isValidContentType && !isValidExtension) {
            throw new AIException(AIErrorCode.INVALID_FILE_TYPE);
        }
    }
}
