package com.oscat.cinema.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oscat.cinema.dao.MovieRepository;
import com.oscat.cinema.dto.CheckoutDataDTO;
import com.oscat.cinema.dto.CinemaDTO;
import com.oscat.cinema.dto.MovieDTO;
import com.oscat.cinema.dto.OrderDTO;
import com.oscat.cinema.dto.ScreeningRoomDTO;
import com.oscat.cinema.dto.SeatDTO;
import com.oscat.cinema.dto.TicketTypeDTO;
import com.oscat.cinema.entity.Member;
import com.oscat.cinema.entity.Movie;
import com.oscat.cinema.entity.Seat;
import com.oscat.cinema.entity.ShowTime;
import com.oscat.cinema.entity.Ticket;
import com.oscat.cinema.service.BookingService;
import com.oscat.cinema.service.MovieService;
import com.oscat.cinema.service.OrderService;
import com.oscat.cinema.service.ScreeningRoomService;
import com.oscat.cinema.service.SeatService;
import com.oscat.cinema.service.ShowTimeManagerService;
import com.oscat.cinema.service.impl.CinameInfoService;

import ecpay.payment.integration.AllInOne;
import ecpay.payment.integration.domain.AioCheckOutOneTime;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping(path = "/official")
public class OfficalBooking {

	@Autowired
	private CinameInfoService infoService;
	@Autowired
	private MovieRepository movRepo;
	@Autowired
	private MovieService movieService;
	@Autowired
	private BookingService bookingService;
	@Autowired
	private ShowTimeManagerService stmService;
	@Autowired
	private SeatService seatService;
	@Autowired
	private ScreeningRoomService srService;
	@Autowired
	private OrderService orderService;

	@GetMapping("/allCinema")
	public ResponseEntity<List<CinemaDTO>> getCinemaIdAndName() {
		List<CinemaDTO> cinemas = infoService.getAllCinemas();
		return ResponseEntity.ok(cinemas);
	}

	@GetMapping("/allScreeningRoom")
	public List<ScreeningRoomDTO> getAllScreeningRoomRoomId(@RequestParam Integer id) {
		return srService.getAllScreeningRoomById(id);
	}

	@GetMapping("/showing")
	public List<MovieDTO> getMovieShowing() {
		return movieService.getMovieShowing();
	}

	@GetMapping("/{movieId}")
	public ResponseEntity<?> findById(@PathVariable UUID movieId) {
		Optional<Movie> optional = movRepo.findById(movieId);

		if (optional.isPresent()) {
			Movie movie = optional.get();
			return new ResponseEntity<Movie>(movie, null, HttpStatus.OK);
		}
		return new ResponseEntity<String>("沒有這筆資料", null, HttpStatus.BAD_REQUEST);
	}

	@GetMapping(path = "/{movieId}/{cinemaId}/findtime")
	public List<Map<String, Object>> findShowTimes(@PathVariable(name = "movieId") UUID movieId,
			@PathVariable(name = "cinemaId") Integer cinemaId) {
		return bookingService.findShowTime(movieId, cinemaId);
	}

	@GetMapping(path = "/ticketTypes")
	public List<TicketTypeDTO> getTicketTypes() {
		return bookingService.getTicketTypesList();
	}

	@GetMapping("/findshowtime/{showTimeId}")
	public ResponseEntity<Map<String, Object>> findShowTimeById(@PathVariable UUID showTimeId) {
		Optional<ShowTime> optionalShowTime = stmService.findShowTimeByIdForOfficial(showTimeId);
		if (optionalShowTime.isPresent()) {
			ShowTime showTime = optionalShowTime.get();
			Integer roomId = showTime.getScreeningRoom().getRoomId();

			Map<String, Object> response = new HashMap<>();
			response.put("showTimeId", showTime.getShowTimeId());
			response.put("filmType", showTime.getFilmType());
			response.put("extraFee", showTime.getExtraFee());
			response.put("showDateAndTime", showTime.getShowDateAndTime());
			response.put("roomId", roomId);
			response.put("transOrders", showTime.getTransOrders());

			return ResponseEntity.ok(response);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@GetMapping("/findAllSeatByRoomId")
	public List<SeatDTO> getSeatsByRoomId(@RequestParam Integer roomId) {
		return seatService.getAllSeatsByRoomIdSortedByName(roomId);
	}

	@GetMapping("/showtime/{showtimeId}/seatIds")
	public ResponseEntity<List<UUID>> getSeatIdsByShowtimeId(@PathVariable String showtimeId) {
		UUID uuid = UUID.fromString(showtimeId);
		List<Ticket> tickets = bookingService.getTicketsByShowtimeId(uuid);

		List<UUID> seatIds = tickets.stream().map(Ticket::getSeat).map(Seat::getSeatId).collect(Collectors.toList());

		return new ResponseEntity<>(seatIds, HttpStatus.OK);
	}

	@PostMapping("/booking")
	public ResponseEntity<String> createOrder(@RequestBody OrderDTO orderDTO, HttpSession session) {

		Member loginMember = (Member) session.getAttribute("loginMember");
		if (loginMember == null) {
			return new ResponseEntity<>("User is not logged in.", HttpStatus.UNAUTHORIZED);
		}
		UUID memberId = loginMember.getMemberId();
		orderDTO.setMemberId(memberId);

		try {
			orderService.createOrder(orderDTO);
			return new ResponseEntity<>("Order created successfully.", HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>("Error creating order: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	/* 準備前往綠界 */
	@PostMapping("/goECPay")
	public void goECpay(@RequestBody CheckoutDataDTO checkoutData, HttpServletResponse response) throws IOException {
		// 設定金流
		AllInOne aio = new AllInOne("");
		AioCheckOutOneTime aioCheck = new AioCheckOutOneTime();
		/* 特店編號 */
		aioCheck.setMerchantID("2000214");
		/* 特店交易時間 */
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		sdf.setLenient(false);
		aioCheck.setMerchantTradeDate(sdf.format(new Date()));
		/* 交易金額 */
		aioCheck.setTotalAmount(checkoutData.getTotalPrice());
		/* 交易描述 */
		aioCheck.setTradeDesc("MovieTicket");
		/* 商品名稱 */
		aioCheck.setItemName("MovieTicket");
		/* 特店交易編號 */
		Random rand = new Random();
		int randomNumber = rand.nextInt(900000) + 100000; // 這將保證生成的是一個六位數的數字
		aioCheck.setMerchantTradeNo("testSpeakitup" + randomNumber);
		/* 付款完成通知回傳網址 */
		aioCheck.setReturnURL("http://localhost:8080/api/offical/returnURL");
		/* Client端回傳付款網址 */
//  setClientBackURL
//    aioCheck.setOrderResultURL();
//    aioCheck.setClientBackURL();
//  輸出畫面
		PrintWriter out = response.getWriter();
		response.setContentType("text/html");
		out.print(aio.aioCheckOut(aioCheck, null));
	}

	@PostMapping("/returnURL")
	public void returnURL(@RequestParam("Rtncode") int Rtncode, @RequestParam("TradeAmt") int TradeAmt,
			HttpServletRequest request) {
		// 交易成功
		if ((request.getRemoteAddr().equalsIgnoreCase("175.99.72.1")
				|| request.getRemoteAddr().equalsIgnoreCase("175.99.72.11")
				|| request.getRemoteAddr().equalsIgnoreCase("175.99.72.24")
				|| request.getRemoteAddr().equalsIgnoreCase("175.99.72.28")
				|| request.getRemoteAddr().equalsIgnoreCase("175.99.72.32")) && Rtncode == 1) {
		}
	}

}
