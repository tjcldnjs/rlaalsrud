package com.library.bookwave.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttribute;

import com.library.bookwave.dto.Payment2DTO;
import com.library.bookwave.dto.PrincipalDTO;
import com.library.bookwave.dto.SignUpDTO;
import com.library.bookwave.handler.exception.DataDeliveryException;
import com.library.bookwave.repository.model.User;
import com.library.bookwave.service.MyPageService;
import com.library.bookwave.service.UserService;
import com.library.bookwave.utils.Define;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class MyPageController {

	private final MyPageService myPageService;
	private final UserService userService;
	
	@Autowired
	PasswordEncoder passwordEncoder;

	/**
	 * 포인트 내역 페이지 이동
	 * 
	 * @return
	 */
	@GetMapping("/pointHistory")
	public String pointHistory(//
			@RequestParam(value = "historyType", defaultValue = "all") String historyType, //
			@RequestParam(name = "sortOrder", defaultValue = "desc") String sortOrder, //
			@RequestParam(name = "sortBy", defaultValue = "date") String sortBy, //
			@RequestParam(name = "page", defaultValue = "1") int page, //
			@RequestParam(name = "size", defaultValue = "5") int size, //
			// TODO 나중에 principal required = false 지워야함
			@SessionAttribute(name = Define.PRINCIPAL, required = false) PrincipalDTO principal, //
			Model model) {
		int userId = principal == null ? 2 : principal.getUserId();

		int startBlock = Math.max(1, ((page - 1) / 5) * 5 + 1);

		int countBalance = myPageService.countBalanceHistory(userId, historyType);
		int totalBalancePages = (int) Math.ceil((double) countBalance / size);
		int endBalanceBlock = Math.min(totalBalancePages, startBlock + 4);

		model.addAttribute("balanceHistory", myPageService.findBalanceHistory(userId, page, size, sortBy, sortOrder,historyType));
		model.addAttribute("totalBalancePages", totalBalancePages);
		model.addAttribute("endBalanceBlock", endBalanceBlock);
		model.addAttribute("startBlock", startBlock);
		model.addAttribute("sortBy", sortBy);
		model.addAttribute("sortOrder", sortOrder);
		model.addAttribute("currentPage", page);
		model.addAttribute("size", size);
		model.addAttribute("historyType", historyType);
		return "/user/pointHistroy";
	}

	@GetMapping("/paymentHistory")
	public String paymentHistory(Model model) {
		int userId = 1;
		List<Payment2DTO> payment = myPageService.findPaymentByUserId(userId);
		model.addAttribute("payment", payment);
		return "/user/paymentHistory";
	}

	/*
	 * 환불 페이지
	 */
	@GetMapping("/paymentRefund/{id}")
	public String paymentRefund(@PathVariable("id") int paymentId,Model model) {
		Payment2DTO payment = myPageService.findPaymentById(paymentId);
		model.addAttribute("payment", payment);
		return "/user/paymentRefund";
	}
	
	
	@PostMapping("/paymentRefund")
	public String paymentRefundw(@RequestParam(name = "id") int paymentId,@RequestParam(name = "cancelReason") String cancelReason) {
		System.out.println(paymentId);
		System.out.println(cancelReason);
		myPageService.updatePayment(paymentId,cancelReason);
		return null;
	}
	
	@GetMapping("/deleteAccount")
	public String deleteAccount() {
		return "/user/deleteAccount";
	}

	@PostMapping("/deleteAccount")
	public String delteAccountw(@RequestParam("password") String password) {
		User user = userService.readUserId("tjcldnjs");
		if (!user.getPassword().equals(password)) {
			throw new DataDeliveryException("비밀번호와 일치 하지않습니다.", HttpStatus.BAD_REQUEST);
		}
		myPageService.deleteUserAccount(user.getId());

		return null;
	}

	/**
	 * 주소 변경 기능
	 * 
	 * @param zip
	 * @param addr1
	 * @param addr2
	 * @return
	 */
	@PostMapping("/changeAddress")
	public String addressChange(@RequestParam("zip") String zip, @RequestParam("addr1") String addr1,
			@RequestParam("addr2") String addr2) {
		if (zip == null || zip.isEmpty()) {
			throw new DataDeliveryException("우편번호가 비어있습니다.", HttpStatus.BAD_REQUEST);
		}
		try {
			myPageService.updateAddress(zip, addr1, addr2, 2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "redirect:/user/mypage";

	}

	/**
	 * 전화번호 변경 기능
	 * 
	 * @param phone
	 * @return
	 */
	@PostMapping("/changePhone")
	public String updatePhone(@RequestParam("phone") String phone) {
		if (phone == null || phone.isEmpty()) {
			throw new DataDeliveryException("전화번호를 입력해주세요.", HttpStatus.BAD_REQUEST);
		}
		if (!(phone.length() == 13) && !(phone.length() == 12)) {
			throw new DataDeliveryException("전화번호를 제대로 입력해주세요.", HttpStatus.BAD_REQUEST);
		}

		try {
			myPageService.updatePhone(phone, 2);
		} catch (Exception e) {
		}
		return "redirect:/user/mypage";
	}

	/**
	 * 개인정보 수정 비밀번호 확인 페이지 이동
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/mypageAuth")
	public String myPageAuth(Model model) {
		User user = userService.readUserId("tjcldnjs");

		model.addAttribute("user", user);
		System.out.println(user.getId());
		return "/user/myPageAuth";
	}

	/**
	 * 개인정보 수정 비밀번호 확인 기능
	 * 
	 * @param password
	 * @return
	 */
	@PostMapping("/mypageAuth")
	public String myPageAuthpost(@RequestParam("password") String password) {
		User user = userService.readUserId("tjcldnjs");
		System.out.println(user.getPassword());
		if (user.getPassword().equals(password)) {
			return "redirect:/user/mypage";
		} else {
			throw new DataDeliveryException("비밀번호가 일치하지않습니다.", HttpStatus.BAD_REQUEST);
		}
	}

	/**
	 * 개인정보 수정 페이지 이동
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/mypage")
	public String myPage(Model model) {
		SignUpDTO user = myPageService.findUserInfoByuserId(2);
		System.out.println("서치원" + user);

		model.addAttribute("user", user);
		return "/user/myPage";
	}

	/**
	 * 비밀번호 변경 페이지 이동
	 * 
	 * @param model
	 * @return
	 */
	@GetMapping("/changePassword")
	public String changePassword(Model model) {

		User user1 = userService.readUserId("tjcldnjs");
		model.addAttribute("user", user1);
		return "/user/changePassword";
	}

	/**
	 * 비밀번호 변경 기능
	 * 
	 * @param currentPassword
	 * @param newPassword
	 * @param confirmPassword
	 * @return
	 */
	//TODO - 컨펌ㅍ채ㅡ워스 지우기
	@PostMapping("/changePassword")
	@ResponseBody
	public ResponseEntity<?> changePasswordw(@RequestBody Map<String, String> request) {
		SignUpDTO user = myPageService.findUserInfoByuserId(2);
		
		String currentPassword = request.get("currentPassword");
		String newPassword = request.get("newPassword");
		System.out.println("똥"  +currentPassword);
		System.out.println("쓰레기" + user.getPassword());
		System.out.println("개쓰레기 " + passwordEncoder.encode(currentPassword));
		System.out.println("개쓰레기 " + passwordEncoder.encode("123"));
		System.out.println("개쓰레기 " + passwordEncoder.encode("123"));
		
		Map<String, Object> response = new HashMap<>();
		
		 // 현재 비밀번호와 입력된 비밀번호 비교
	    if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
	    	System.out.println("이거 왜 안뜨냐");
	    	response.put("success", false);
	    	response.put("message", "비밀번호가 일치하지 않습니다.");
	    	return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
	    }
	    
	    // TODO 비밀번호가 일치 했을 때 코드 진행
	    // 1. newPassword를 암호화
	    // 2. 암호화된 패스워드를 update
	    // 3. 서비스로 부터 true / false 값을 받아와서
	    String encodedNewPassword = passwordEncoder.encode(newPassword);
	    System.out.println("쓰레기" + encodedNewPassword);
	    if (myPageService.updateUserByPassword(encodedNewPassword, 2)) {
	    	response.put("success", true);
	    	response.put("message", "비밀번호 변경 성공");
	    	return ResponseEntity.status(HttpStatus.OK).body(response);
	    } else {
	    	response.put("success", false);
	    	response.put("message", "서버 오류 발생");
	    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }


	}
}
