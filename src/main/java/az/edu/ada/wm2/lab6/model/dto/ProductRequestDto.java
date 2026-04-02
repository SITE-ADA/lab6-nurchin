package az.edu.ada.wm2.lab6.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
public class ProductRequestDto {
    private String productName;
    private BigDecimal price;
    private LocalDate expirationDate;
    private List<UUID> categoryIds;
}
