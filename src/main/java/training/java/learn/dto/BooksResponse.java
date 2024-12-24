package training.java.learn.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BooksResponse {
    private String judul;
    private String penulis;
    private String penerbit;
}
