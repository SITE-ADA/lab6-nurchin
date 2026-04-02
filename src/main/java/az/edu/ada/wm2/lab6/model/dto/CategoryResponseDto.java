package az.edu.ada.wm2.lab6.model.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class CategoryResponseDto {
    private UUID id;
    private String name;
}