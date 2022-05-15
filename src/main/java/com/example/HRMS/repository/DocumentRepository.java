package com.example.HRMS.repository;

import com.example.HRMS.entity.Document;
import com.example.HRMS.entity.User;
import com.example.HRMS.enums.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository  extends JpaRepository<Document,Long> {

    //Document findDocumentByDocumentTypeAndUser(DocumentType type, User user);

    List<Document> findDocumentByDocumentTypeAndUser(DocumentType type, User user);

    boolean existsByUrl(String url);

    Document findByUrl(String url);

    Document deleteByUrl(String url);

    boolean existsDocumentByUser(User user);

    Document findDocumentByUserAndDocumentType(User user, DocumentType type);
}
