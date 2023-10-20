package com.oscat.cinema.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

@Data
public class MovieDTO {

	private String movieName;
    private String movieType;
    private String movieStatus;
    private String director;
    private String writerList;
    private String actorList;
    private String plotSummary;
    private Date releaseDate;
    private Integer duration;
    private String classification;
    private String trailerLink;
    private String posterImage;
    private List<MovieStillsDTO> movieStills;
}
