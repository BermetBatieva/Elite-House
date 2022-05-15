package com.example.HRMS.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data

public class DiagramAndResult {

    private List<MainPageDiagram> diagramList;
    private CompletedTest completedTest;
}
