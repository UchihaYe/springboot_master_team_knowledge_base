package com.teamkb.service;

import com.teamkb.dto.DocumentDto;
import com.teamkb.entity.*;
import com.teamkb.exception.ResourceNotFoundException;
import com.teamkb.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final SpaceRepository spaceRepository;
    private final UserRepository userRepository;
    private final TagRepository tagRepository;
    private final DocumentTagRepository documentTagRepository;
    private final DocumentVersionRepository documentVersionRepository;

    @Autowired
    public DocumentService(DocumentRepository documentRepository,
                          SpaceRepository spaceRepository,
                          UserRepository userRepository,
                          TagRepository tagRepository,
                          DocumentTagRepository documentTagRepository,
                          DocumentVersionRepository documentVersionRepository) {
        this.documentRepository = documentRepository;
        this.spaceRepository = spaceRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
        this.documentTagRepository = documentTagRepository;
        this.documentVersionRepository = documentVersionRepository;
    }

    public DocumentDto createDocument(DocumentDto documentDto, Long authorId) {
        User author = userRepository.findById(authorId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + authorId));

        Space space = spaceRepository.findById(documentDto.getSpaceId())
            .orElseThrow(() -> new ResourceNotFoundException("Space not found with id: " + documentDto.getSpaceId()));

        Document document = new Document();
        document.setTitle(documentDto.getTitle());
        document.setContent(documentDto.getContent());
        document.setPath(generatePath(documentDto.getTitle(), space, documentDto.getParentId()));
        document.setType(documentDto.getType() != null ? documentDto.getType() : Document.DocumentType.DOCUMENT);
        document.setSpace(space);
        document.setAuthor(author);

        if (documentDto.getParentId() != null) {
            Document parent = documentRepository.findById(documentDto.getParentId())
                .orElseThrow(() -> new ResourceNotFoundException("Parent document not found"));
            document.setParent(parent);
        }

        if (documentDto.getReviewCycleDays() != null) {
            document.setReviewCycleDays(documentDto.getReviewCycleDays());
            document.calculateNextReviewDate();
        }

        Document savedDocument = documentRepository.save(document);

        // Handle tags
        if (documentDto.getTags() != null && !documentDto.getTags().isEmpty()) {
            addTagsToDocument(savedDocument, documentDto.getTags());
        }

        // Create initial version
        createDocumentVersion(savedDocument, author, "Initial version");

        return DocumentDto.fromEntity(savedDocument);
    }

    @Transactional(readOnly = true)
    public DocumentDto getDocumentById(Long id) {
        Document document = documentRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));
        return DocumentDto.fromEntity(document);
    }

    @Transactional(readOnly = true)
    public List<DocumentDto> getDocumentsBySpace(Long spaceId) {
        Space space = spaceRepository.findById(spaceId)
            .orElseThrow(() -> new ResourceNotFoundException("Space not found with id: " + spaceId));

        return documentRepository.findActiveBySpace(space).stream()
            .map(DocumentDto::fromEntity)
            .collect(Collectors.toList());
    }

    public DocumentDto updateDocument(Long id, DocumentDto documentDto, Long userId) {
        Document document = documentRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));

        User user = userRepository.findById(userId)
            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        boolean contentChanged = false;

        if (documentDto.getTitle() != null && !documentDto.getTitle().equals(document.getTitle())) {
            document.setTitle(documentDto.getTitle());
            contentChanged = true;
        }

        if (documentDto.getContent() != null && !documentDto.getContent().equals(document.getContent())) {
            document.setContent(documentDto.getContent());
            contentChanged = true;
        }

        if (documentDto.getReviewCycleDays() != null) {
            document.setReviewCycleDays(documentDto.getReviewCycleDays());
            document.calculateNextReviewDate();
        }

        if (contentChanged) {
            document.incrementVersion();
            createDocumentVersion(document, user, "Document updated");
        }

        Document updatedDocument = documentRepository.save(document);

        // Update tags
        if (documentDto.getTags() != null) {
            updateDocumentTags(updatedDocument, documentDto.getTags());
        }

        return DocumentDto.fromEntity(updatedDocument);
    }

    public void deleteDocument(Long id) {
        Document document = documentRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));

        document.markAsDeleted();
        documentRepository.save(document);
    }

    public DocumentDto viewDocument(Long id) {
        Document document = documentRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));

        document.incrementViewCount();
        Document updatedDocument = documentRepository.save(document);

        return DocumentDto.fromEntity(updatedDocument);
    }

    public void archiveDocument(Long id) {
        Document document = documentRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));

        document.archive();
        documentRepository.save(document);
    }

    public void unarchiveDocument(Long id) {
        Document document = documentRepository.findByIdAndDeletedAtIsNull(id)
            .orElseThrow(() -> new ResourceNotFoundException("Document not found with id: " + id));

        document.unarchive();
        documentRepository.save(document);
    }

    @Transactional(readOnly = true)
    public Page<DocumentDto> searchDocuments(String keyword, Pageable pageable) {
        return documentRepository.searchByKeyword(keyword, pageable)
            .map(DocumentDto::fromEntity);
    }

    @Transactional(readOnly = true)
    public List<DocumentDto> getDocumentsByTag(String tagName) {
        return documentRepository.findByTagName(tagName).stream()
            .map(DocumentDto::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DocumentDto> getDocumentsNeedingReview() {
        return documentRepository.findDocumentsNeedingReview(LocalDateTime.now()).stream()
            .map(DocumentDto::fromEntity)
            .collect(Collectors.toList());
    }

    private String generatePath(String title, Space space, Long parentId) {
        String basePath = space.getName() + "/" + title;
        if (parentId != null) {
            Document parent = documentRepository.findById(parentId).orElse(null);
            if (parent != null) {
                basePath = parent.getPath() + "/" + title;
            }
        }
        return basePath.replaceAll("[^a-zA-Z0-9/\\-_]", "-").toLowerCase();
    }

    private void addTagsToDocument(Document document, List<String> tagNames) {
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName).orElseGet(() -> {
                Tag newTag = new Tag(tagName);
                return tagRepository.save(newTag);
            });

            DocumentTag documentTag = new DocumentTag(document, tag);
            documentTagRepository.save(documentTag);
            tag.incrementUsageCount();
            tagRepository.save(tag);
        }
    }

    private void updateDocumentTags(Document document, List<String> tagNames) {
        // Remove existing tags
        Set<DocumentTag> existingTags = document.getTags();
        for (DocumentTag dt : existingTags) {
            Tag tag = dt.getTag();
            tag.decrementUsageCount();
            tagRepository.save(tag);
        }
        documentTagRepository.deleteAll(existingTags);

        // Add new tags
        addTagsToDocument(document, tagNames);
    }

    private void createDocumentVersion(Document document, User author, String changeSummary) {
        DocumentVersion version = new DocumentVersion(
            document, author, document.getVersion(),
            document.getTitle(), document.getContent()
        );
        version.setChangeSummary(changeSummary);
        documentVersionRepository.save(version);
    }
}