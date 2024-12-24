package training.java.learn.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateBookRequest {
    @NotBlank
    @Size(max = 50)
    private String judul;
    @NotBlank
    @Size(max = 50)
    private String penulis;
    @NotBlank
    @Size(max = 50)
    private String penerbit;
}
