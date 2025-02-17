package training.java.learn.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateAuthorRequest {

    @NotBlank
    @Size(max = 50)
    @JsonProperty("author_name")
    private String authorName;

    @NotBlank
    @Size(max = 200)
    private String address;

    public CreateAuthorRequest(String authorName, String address) {
        super();
        this.authorName = authorName;
        this.address = address;
    }
}
