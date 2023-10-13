package com.oscat.cinema.entity;
import lombok.Data;
import jakarta.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "movie_stills")
public class MovieStills {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(columnDefinition = "uniqueidentifier", name = "still_id")
    private UUID stillId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "movie_id", nullable = false)
    private Movie movie;

    @Column(name = "still_image_url", nullable = false, length = 1000)
    private String stillImageUrl;
}
