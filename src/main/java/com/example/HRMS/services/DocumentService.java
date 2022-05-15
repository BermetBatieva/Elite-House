package com.example.HRMS.services;

import com.example.HRMS.DTO.ResponseMessage;
import com.example.HRMS.entity.Document;
import com.example.HRMS.entity.User;
import com.example.HRMS.enums.DocumentType;
import com.example.HRMS.enums.Status;
import com.example.HRMS.repository.DocumentRepository;
import com.example.HRMS.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class DocumentService {

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    ResourceLoader resourceLoader;

    @Value("${upload.path}")
    private String uploadPath;

    @Transactional
    public ResponseMessage create(MultipartFile resume, MultipartFile[] recommendation,
                                  MultipartFile[] additionalFiles) {
        try {
            User user = userDetailsService.getCurrentUser();
            if (resume != null) {
                Document document = new Document();
                document.setDocumentType(DocumentType.RESUME);
                document.setName(user.getEmail() + "-resume");
                document.setStatus(Status.ACTIVE);
                document.setUser(user);
                document.setUrl(set(resume, user.getEmail() + "-resume"));
                documentRepository.save(document);
            }

            int j = 1;
            for (MultipartFile file : recommendation) {
                if (file != null) {
                    Document document = new Document();
                    document.setDocumentType(DocumentType.RECOMMENDATION_LETTER);
                    document.setName(user.getEmail() + "-recommendation" + j);
                    document.setStatus(Status.ACTIVE);
                    document.setUser(user);
                    document.setUrl(set(file, user.getEmail() + "-recommendation" + j));
                    documentRepository.save(document);
                    j++;
                }
            }

            int i = 1;
            for (MultipartFile file : additionalFiles) {
                if (resume != null) {
                    Document document = new Document();
                    document.setDocumentType(DocumentType.ADDITIONAL_FILES);
                    document.setName(user.getEmail() + "-ADDITIONAL_FILES_" + i);
                    document.setStatus(Status.ACTIVE);
                    document.setUser(user);
                    document.setUrl(set(file, user.getEmail() + "-ADDITIONAL_FILES_" + i));
                    documentRepository.save(document);
                    i++;
                }
            }
            return new ResponseMessage("successfully", 200);
        } catch (Exception e) {
            return new ResponseMessage("file processing error! - documents not added\n" + e.getMessage(), 400);
        }
    }

    @Transactional
    public ResponseMessage addRecommendations(MultipartFile[] recommendations) {
        try {
            User user = userDetailsService.getCurrentUser();
            int j = 1;
            for (MultipartFile file : recommendations) {
                if (file != null) {
                    Document document = new Document();
                    document.setDocumentType(DocumentType.RECOMMENDATION_LETTER);
                    document.setName(user.getEmail() + "-recommendation" + j);
                    document.setStatus(Status.ACTIVE);
                    document.setUser(user);
                    document.setUrl(set(file, user.getEmail() + "-recommendation" + j));
                    documentRepository.save(document);
                    j++;
                }
            }
            return new ResponseMessage("Success", 200);
        } catch (Exception e) {
            return new ResponseMessage("File processing error!" + e.getMessage(), 400);
        }
    }

    @Transactional
    public ResponseMessage addAdditionalFiles(MultipartFile[] additionalFiles) {
        try {
            User user = userDetailsService.getCurrentUser();
            int i = 1;
            for (MultipartFile file : additionalFiles) {
                if (file != null) {
                    Document document = new Document();
                    document.setDocumentType(DocumentType.ADDITIONAL_FILES);
                    document.setName(user.getEmail() + "-ADDITIONAL_FILES_" + i);
                    document.setStatus(Status.ACTIVE);
                    document.setUser(user);
                    document.setUrl(set(file, user.getEmail() + "-ADDITIONAL_FILES_" + i));
                    documentRepository.save(document);
                    i++;
                }
            }
            return new ResponseMessage("Success", 200);
        } catch (Exception e) {
            return new ResponseMessage("File processing error!" + e.getMessage(), 400);
        }
    }

    @Transactional
    public ResponseMessage updateResume(MultipartFile resume) {
        try {
            User user = userDetailsService.getCurrentUser();
            if (resume != null) {
                Document document = documentRepository.findDocumentByDocumentTypeAndUser(DocumentType.RESUME, user).get(0);
                document.setDocumentType(DocumentType.RESUME);
                document.setName(user.getEmail() + "-resume");
                document.setStatus(Status.ACTIVE);
                document.setUser(user);
                document.setUrl(set(resume, user.getEmail() + "-resume"));
                documentRepository.save(document);
            }
            return new ResponseMessage("successfully updated", 200);
        } catch (Exception e) {
            return new ResponseMessage("File processing error! - documents are not added\n" + e.getMessage(), 400);
        }
    }


    public ResponseMessage deleteDocumentById(Long id) {
        if (!documentRepository.existsById(id))
            return new ResponseMessage("File not found", 404);
        documentRepository.deleteById(id);
        return new ResponseMessage("Deleted", 200);
    }

    @Transactional
    public ResponseMessage addProfilePhoto(MultipartFile photo) {
        try {
            User user = userDetailsService.getCurrentUser();
            if (photo != null) {
                Document document = new Document();
                document.setDocumentType(DocumentType.PHOTO);
                document.setName(user.getEmail() + "-photo");
                document.setStatus(Status.ACTIVE);
                document.setUser(user);
                document.setUrl(set(photo, user.getEmail() + "-photo"));
                user.setPhotoUrl(set(photo, user.getEmail() + "-photo"));
                documentRepository.save(document);
                userRepository.save(user);
            }
            return new ResponseMessage("Succesfully set", 200);
        } catch (Exception e) {
            return new ResponseMessage("File processing error! " + e.getMessage(), 400);
        }
    }

    @Transactional
    public ResponseMessage deletePhotoById(Long id) {
        if (!documentRepository.existsById(id))
            return new ResponseMessage("No such file found!", 404);
        documentRepository.deleteById(id);
        return new ResponseMessage("Deleted", 200);
    }

    @Transactional
    public ResponseMessage deletePhotoByUrl(String url) {
        if (!documentRepository.existsByUrl(url))
            return new ResponseMessage("No such file found!", 404);
        documentRepository.deleteByUrl(url);
        return new ResponseMessage("Deleted", 200);
    }

    public ResponseMessage updatePhoto(MultipartFile photo) {
        try {
            User user = userDetailsService.getCurrentUser();
            Document old = documentRepository.findDocumentByUserAndDocumentType(user, DocumentType.PHOTO);
            old.setUrl(set(photo, user.getEmail() + "-photo"));
            user.setPhotoUrl(set(photo, user.getEmail() + "-photo"));
            documentRepository.save(old);
            userRepository.save(user);
            return new ResponseMessage("Succesfully set", 200);
        } catch (Exception e) {
            return new ResponseMessage("File processing error! " + e.getMessage(), 400);
        }
    }

    private String set(MultipartFile file, String urlName) {
        try {
            byte[] bytes = file.getBytes();
            Path path = Paths.get(uploadPath + urlName + getFileExtension(file.getOriginalFilename()));
            Files.write(path, bytes);
            return path.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private String getFileExtension(String originalFilename) {
        StringBuilder result = new StringBuilder();
        int index = originalFilename.length() - 1;
        char ch = originalFilename.charAt(index);
        while (ch != '.') {
            result.append(ch);
            index--;
            ch = originalFilename.charAt(index);
        }
        return result.append('.').reverse().toString();
    }

    public ResponseMessage deleteById(Long id) {
        Document document = documentRepository.findById(id).orElse(null);

        if (document == null)
            return new ResponseMessage("Документ с id = " + id + " не найден", 404);
        deleteImage(document.getUrl());
        document.setStatus(Status.REMOVED);
        documentRepository.save(document);
        return new ResponseMessage("Документ удален!", 200);
    }

    //TODO что за метод???
    public ResponseEntity<Object> getById(Long id) throws FileNotFoundException {
        Document document = documentRepository.findById(id).orElse(null);
        File file = new File(document.getUrl());
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        ResponseEntity<Object>
                responseEntity = ResponseEntity.ok().headers(headers).contentLength(
                file.length()).contentType(MediaType.parseMediaType("application/txt")).body(resource);

        return responseEntity;
    }

    private synchronized static void deleteImage(String imagePath) {
        try {
            File file = new File(imagePath);
            if (file.delete()) {
                System.out.println(file.getName() + " is deleted!");
            } else {
                System.out.println("Delete operation is failed.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
