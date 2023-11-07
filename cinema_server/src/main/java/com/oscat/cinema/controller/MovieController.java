package com.oscat.cinema.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.oscat.cinema.dao.MovieRepository;
import com.oscat.cinema.dto.MovieDTO;
import com.oscat.cinema.entity.Movie;
import com.oscat.cinema.service.MovieService;
import com.oscat.cinema.util.aijie.FileUploadUtil;

import jakarta.transaction.Transactional;

@RestController
@RequestMapping(path = "/api/movie")
public class MovieController {

	@Autowired
	private MovieRepository movRepo;

	@Autowired
	private MovieService movieService;

	@Autowired
	FileUploadUtil fileUploadUtil;

	// 新增電影
	@PostMapping("/add")
	public ResponseEntity<?> postAddMovie(@RequestBody Movie movie) {

		Movie result = movieService.saveMovie(movie);

		if (result != null) {
			return new ResponseEntity<Movie>(result, null, HttpStatus.OK);
		}
		return new ResponseEntity<String>("新增失敗，請檢查輸入格式", null, HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/addList")
	public List<Movie> addListMovie(@RequestBody List<Movie> MovieList) {
		return movRepo.saveAll(MovieList);
	}

	@GetMapping("movie/byName/{movieName}")
	public ResponseEntity<?> findBymovieName(@PathVariable String movieName) {
		Optional<Movie> optional = movRepo.findBymovieName(movieName);

		if (optional.isPresent()) {
			Movie result = optional.get();
			return new ResponseEntity<Movie>(result, null, HttpStatus.OK);
		}

		return new ResponseEntity<String>("沒有這筆資料", null, HttpStatus.BAD_REQUEST);
	}

	// 根據id查詢電影
	@GetMapping("/{movieId}")
	public ResponseEntity<?> findById(@PathVariable UUID movieId) {
		Optional<Movie> optional = movRepo.findById(movieId);

		if (optional.isPresent()) {
			Movie movie = optional.get();
			return new ResponseEntity<Movie>(movie, null, HttpStatus.OK);
		}
		return new ResponseEntity<String>("沒有這筆資料", null, HttpStatus.BAD_REQUEST);
	}

	// 查詢所有電影
	@GetMapping("/")
	public List<Movie> findAllMovie() {
		return movRepo.findAll();
	}

	// 根據上映狀態查詢電影
	@GetMapping("/showing")
	public List<MovieDTO> getMovieShowing() {
		return movieService.getMovieShowing();
	}

	// 根據id刪除電影
	@DeleteMapping("/delete/{movieId}")
	public String deleteMovieById(@PathVariable UUID movieId) {
		return movieService.deleteMovie(movieId);
	}

	// 根據id修改電影
	@Transactional
	@PutMapping("/update/{movieId}")
	public String updateMovieById(@PathVariable UUID movieId, @RequestBody MovieDTO newMovieDTO) {
		Optional<Movie> optional = movieService.updateMovie(movieId, newMovieDTO);

		if (optional.isEmpty()) {
			return "無此筆資料";
		}
		return "修改完成";
	}

	@GetMapping("/page/{pageNumber}")
	public Page<Movie> findMovieByPage(@PathVariable Integer pageNumber) {
		PageRequest pgb = PageRequest.of(pageNumber - 1, 2, Sort.Direction.ASC, "movieId");
		Page<Movie> page = movRepo.findAll(pgb);
		return page;

	}

	@GetMapping("/name")
	public List<Movie> findMovieByNameNativeQuery(@RequestParam("n") String movieName) {
		return movRepo.findMovieByNameNativeQuery(movieName);
	}

	@GetMapping("/nameLike")
	public List<Movie> findMovieByNameLike(@RequestParam("n") String name) {
		return movRepo.findMovieByNameLike(name);
	}

	// 根據id修改電影狀態
	@PutMapping("/status")
	public ResponseEntity<String> updateStatusById(@RequestBody String json) throws JSONException {
		System.out.println(json);
		JSONObject joo = new JSONObject(json);
		String status = joo.getString("movieStatus");
		String id = joo.getString("movieId");
		UUID movieId = UUID.fromString(id);
		boolean result = movieService.updateStatusById(status, movieId);
		if (result) {
			return new ResponseEntity<String>("狀態更新", null, HttpStatus.OK);
		}
		return new ResponseEntity<String>("更新失敗", null, HttpStatus.BAD_REQUEST);
	}

	// 分頁所有電影
	@GetMapping("/page")
	public ResponseEntity<Page<Movie>> getAllMovies(Pageable pageable) {
		Page<Movie> movies = movRepo.findAll(pageable);
		return ResponseEntity.ok(movies);
	}

	// 上傳圖檔到雲端圖庫
	@PostMapping("/upload")
	public ResponseEntity<String> upload(@RequestParam("imageUpload") MultipartFile imageFile) {
		String imgurl = "";
		try {
			imgurl = fileUploadUtil.uploadFile(imageFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(imgurl);
		return ResponseEntity.ok(imgurl);
	}

}
