package com.oscat.cinema.util;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Pagination {

    private Integer page;

    private Integer pageSize;

    private Integer total;
}
