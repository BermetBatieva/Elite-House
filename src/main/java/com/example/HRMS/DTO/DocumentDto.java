package com.example.HRMS.DTO;

import com.example.HRMS.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DocumentDto {

    private DocumentType documentType;

    private String url;

    private String name; //зачем нужно   -----> это воообще что такое!
}
