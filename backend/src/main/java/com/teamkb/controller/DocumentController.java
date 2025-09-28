package com.teamkb.controller;

import com.teamkb.dto.ApiResponse;
import com.teamkb.dto.DocumentDto;
import com.teamkb.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/documents")
@Tag(name = "Document Management", description = "Document management APIs")
public class DocumentController {

    private final DocumentService documentService;

    @Autowired
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping
    @Operation(summary = "Create a new document")
    public ResponseEntity<ApiResponse<DocumentDto>> createDocument(
            @Valid @RequestBody DocumentDto documentDto,
            @RequestParam Long authorId) {
        try {
            DocumentDto createdDocument = documentService.createDocument(documentDto, authorId);
            return ResponseEntity.ok(ApiResponse.success("Document created successfully", createdDocument));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get document by ID")
    public ResponseEntity<ApiResponse<DocumentDto>> getDocumentById(@PathVariable Long id) {
        try {
            DocumentDto document = documentService.getDocumentById(id);
            return ResponseEntity.ok(ApiResponse.success(document));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/view")
    @Operation(summary = "View document (increments view count)")
    public ResponseEntity<ApiResponse<DocumentDto>> viewDocument(@PathVariable Long id) {
        try {
            DocumentDto document = documentService.viewDocument(id);
            return ResponseEntity.ok(ApiResponse.success(document));
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update document")
    public ResponseEntity<ApiResponse<DocumentDto>> updateDocument(
            @PathVariable Long id,
            @Valid @RequestBody DocumentDto documentDto,
            @RequestParam Long userId) {
        try {
            DocumentDto updatedDocument = documentService.updateDocument(id, documentDto, userId);
            return ResponseEntity.ok(ApiResponse.success("Document updated successfully", updatedDocument));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete document")
    public ResponseEntity<ApiResponse<String>> deleteDocument(@PathVariable Long id) {
        try {
            documentService.deleteDocument(id);
            return ResponseEntity.ok(ApiResponse.success("Document deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{id}/archive")
    @Operation(summary = "Archive document")
    public ResponseEntity<ApiResponse<String>> archiveDocument(@PathVariable Long id) {
        try {
            documentService.archiveDocument(id);
            return ResponseEntity.ok(ApiResponse.success("Document archived successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping("/{id}/unarchive")
    @Operation(summary = "Unarchive document")
    public ResponseEntity<ApiResponse<String>> unarchiveDocument(@PathVariable Long id) {
        try {
            documentService.unarchiveDocument(id);
            return ResponseEntity.ok(ApiResponse.success("Document unarchived successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(e.getMessage()));
        }
    }

    @GetMapping("/space/{spaceId}")
    @Operation(summary = "Get documents by space")
    public ResponseEntity<ApiResponse<List<DocumentDto>>> getDocumentsBySpace(@PathVariable Long spaceId) {
        List<DocumentDto> documents = documentService.getDocumentsBySpace(spaceId);
        return ResponseEntity.ok(ApiResponse.success(documents));
    }

    @GetMapping("/search")
    @Operation(summary = "Search documents")
    public ResponseEntity<ApiResponse<Page<DocumentDto>>> searchDocuments(
            @RequestParam String keyword,
            Pageable pageable) {
        Page<DocumentDto> documents = documentService.searchDocuments(keyword, pageable);
        return ResponseEntity.ok(ApiResponse.success(documents));
    }

    @GetMapping("/tag/{tagName}")
    @Operation(summary = "Get documents by tag")
    public ResponseEntity<ApiResponse<List<DocumentDto>>> getDocumentsByTag(@PathVariable String tagName) {
        List<DocumentDto> documents = documentService.getDocumentsByTag(tagName);
        return ResponseEntity.ok(ApiResponse.success(documents));
    }

    @GetMapping("/review-needed")
    @Operation(summary = "Get documents needing review")
    public ResponseEntity<ApiResponse<List<DocumentDto>>> getDocumentsNeedingReview() {
        List<DocumentDto> documents = documentService.getDocumentsNeedingReview();
        return ResponseEntity.ok(ApiResponse.success(documents));
    }
}