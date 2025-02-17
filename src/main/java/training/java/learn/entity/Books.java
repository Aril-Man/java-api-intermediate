package training.java.learn.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Table(name = "books")
public class Books {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    private String judul;
    private String penulis;
    private String penerbit;

    @ManyToOne
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    private Author author;

    public Books(String judul, String penulis, String penerbit) {
        this.judul = judul;
        this.penulis = penulis;
        this.penerbit = penerbit;
    }
}
