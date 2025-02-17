package training.java.learn.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "author")
@Setter
@Getter
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "author_name")
    private String authorName;

    @Column(name = "address")
    private String address;

    @OneToMany(mappedBy = "author")
    private List<Books> books;

    public Author(String authorName, String address) {
        super();
        this.authorName = authorName;
        this.address = address;
    }
}
