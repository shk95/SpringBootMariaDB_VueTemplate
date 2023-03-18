package kopo.poly.web.apis;

import kopo.poly.dto.MsgDTO;
import kopo.poly.dto.NoticeDTO;
import kopo.poly.service.INoticeService;
import kopo.poly.util.CmmUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * Controller 선언해야만 Spring 프레임워크에서 Controller인지 인식 가능
 * 자바 서블릿 역할 수행
 *
 * slf4j는 스프링 프레임워크에서 로그 처리하는 인터페이스 기술이며,
 * 로그처리 기술인 log4j와 logback과 인터페이스 역할 수행함
 * 스프링 프레임워크는 기본으로 logback을 채택해서 로그 처리함
 * */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notice")
public class NoticeApiController {

	// @RequiredArgsConstructor 를 통해 메모리에 올라간 서비스 객체를 Controller에서 사용할 수 있게 주입함
	private final INoticeService noticeService;

	/**
	 * 게시판 리스트 보여주기
	 * <p>
	 * GetMapping(value = "notice/noticeList") =>  GET방식을 통해 접속되는 URL이 notice/noticeList 경우 아래 함수를 실행함
	 */
	@GetMapping("/list-all")
	public ResponseEntity<List<NoticeDTO>> getNoticeList(HttpSession session) throws Exception {
		// 로그 찍기(추후 찍은 로그를 통해 이 함수에 접근했는지 파악하기 용이하다.)
		log.info(this.getClass().getName() + ".noticeList Start!");

		// 로그인된 사용자 아이디는 Session에 저장함
		// 교육용으로 아직 로그인을 구현하지 않았기 때문에 Session에 데이터를 저장하지 않았음
		// 추후 로그인을 구현할 것으로 가정하고, 공지사항 리스트 출력하는 함수에서 로그인 한 것처럼 Session 값을 생성함
		session.setAttribute("SESSION_USER_ID", "USER01");

		// 공지사항 리스트 조회하기
		// Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
		List<NoticeDTO> rList = Optional.ofNullable(noticeService.getNoticeList())
			.orElseGet(ArrayList::new);

		// 로그 찍기(추후 찍은 로그를 통해 이 함수 호출이 끝났는지 파악하기 용이하다.)
		log.info(this.getClass().getName() + ".noticeList End!");
		return ResponseEntity.ok(rList);
	}

	/**
	 * 게시판 상세보기
	 */
	@GetMapping("/{nSeq}")
	public ResponseEntity<NoticeDTO> getNoticeDetail(@PathVariable String nSeq) throws Exception {

		log.info(this.getClass().getName() + ".noticeInfo Start!");

		log.info("nSeq : " + nSeq);
		/*
		 * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
		 */
		NoticeDTO pDTO = new NoticeDTO();
		pDTO.setNoticeSeq(Long.parseLong(nSeq)); // String 타입을 long 타입으로 변경

		// 공지사항 상세정보 가져오기
		// Java 8부터 제공되는 Optional 활용하여 NPE(Null Pointer Exception) 처리
		NoticeDTO rDTO = Optional.ofNullable(noticeService.getNoticeInfo(pDTO, true))
			.orElseGet(NoticeDTO::new);

		log.info(this.getClass().getName() + ".noticeInfo End!");
		return ResponseEntity.ok(rDTO);
	}

	/**
	 * 게시판 글 등록
	 * <p>
	 * 게시글 등록은 Ajax로 호출되기 때문에 결과는 JSON 구조로 전달해야만 함
	 * JSON 구조로 결과 메시지를 전송하기 위해 @ResponseBody 어노테이션 추가함
	 */
	@PostMapping("/")
	public ResponseEntity<MsgDTO> submitNotice(@RequestBody NoticeDTO pDTO, HttpSession session) {

		log.info(this.getClass().getName() + ".noticeInsert Start!");

		String msg = ""; // 메시지 내용
		MsgDTO dto = null; // 결과 메시지 구조

		try {
			// 로그인된 사용자 아이디를 가져오기
			// 로그인을 아직 구현하지 않았기에 공지사항 리스트에서 로그인 한 것처럼 Session 값을 저장함
			String userId = CmmUtil.nvl((String) session.getAttribute("SESSION_USER_ID"));

			/*
			 * ####################################################################################
			 * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함 반드시 작성할 것
			 * ####################################################################################
			 */
			log.info("session user_id : " + userId);
			log.info("title : " + pDTO.getTitle());
			log.info("noticeYn : " + pDTO.getNoticeYn());
			log.info("contents : " + pDTO.getContents());

			// 데이터 저장하기 위해 DTO에 저장하기
			pDTO.setUserId(userId);

			/*
			 * 게시글 등록하기위한 비즈니스 로직을 호출
			 */
			noticeService.insertNoticeInfo(pDTO);

			// 저장이 완료되면 사용자에게 보여줄 메시지
			msg = "등록되었습니다.";
		} catch (Exception e) {
			// 저장이 실패되면 사용자에게 보여줄 메시지
			msg = "실패하였습니다. : " + e.getMessage();
			log.info(e.toString());
			e.printStackTrace();
		} finally {
			// 결과 메시지 전달하기
			dto = new MsgDTO();
			dto.setMsg(msg);

			log.info(this.getClass().getName() + ".noticeInsert End!");
		}

		return ResponseEntity.ok(dto);
	}

	/**
	 * 게시판 글 수정
	 */
	@PutMapping(value = "/{nSeq}")
	public ResponseEntity<MsgDTO> noticeUpdate(HttpSession session, @PathVariable String nSeq,
	                                           @RequestBody NoticeDTO pDTO) {

		log.info(this.getClass().getName() + ".noticeUpdate Start!");

		String msg = ""; // 메시지 내용
		MsgDTO dto = null; // 결과 메시지 구조

		try {
			String userId = CmmUtil.nvl((String) session.getAttribute("SESSION_USER_ID")); // 아이디

			/*
			 * ####################################################################################
			 * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함 반드시 작성할 것
			 * ####################################################################################
			 */
			log.info("userId : " + userId);
			log.info("nSeq : " + nSeq);
			log.info("title : " + pDTO.getTitle());
			log.info("noticeYn : " + pDTO.getNoticeYn());
			log.info("contents : " + pDTO.getContents());

			/*
			 * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
			 */
			pDTO.setUserId(userId);
			pDTO.setNoticeSeq(Long.parseLong(nSeq)); // String 타입을 long 타입으로 변경

			// 게시글 수정하기 DB
			noticeService.updateNoticeInfo(pDTO);

			msg = "수정되었습니다.";
		} catch (Exception e) {
			msg = "실패하였습니다. : " + e.getMessage();
			log.info(e.toString());
			e.printStackTrace();
		} finally {

			// 결과 메시지 전달하기
			dto = new MsgDTO();
			dto.setMsg(msg);

			log.info(this.getClass().getName() + ".noticeUpdate End!");
		}

		return ResponseEntity.ok(dto);
	}

	/**
	 * 게시판 글 삭제
	 */
	@ResponseBody
	@DeleteMapping("/{nSeq}")
	public ResponseEntity<MsgDTO> noticeDelete(@PathVariable String nSeq) {

		log.info(this.getClass().getName() + ".noticeDelete Start!");

		String msg = ""; // 메시지 내용
		MsgDTO dto = null; // 결과 메시지 구조

		try {
			/*
			 * ####################################################################################
			 * 반드시, 값을 받았으면, 꼭 로그를 찍어서 값이 제대로 들어오는지 파악해야함 반드시 작성할 것
			 * ####################################################################################
			 */
			log.info("nSeq : " + nSeq);

			/*
			 * 값 전달은 반드시 DTO 객체를 이용해서 처리함 전달 받은 값을 DTO 객체에 넣는다.
			 */
			NoticeDTO pDTO = new NoticeDTO();
			pDTO.setNoticeSeq(Long.parseLong(nSeq)); // String 타입을 long 타입으로 변경

			// 게시글 삭제하기 DB
			noticeService.deleteNoticeInfo(pDTO);

			msg = "삭제되었습니다.";
		} catch (Exception e) {
			msg = "실패하였습니다. : " + e.getMessage();
			log.info(e.toString());
			e.printStackTrace();
		} finally {
			// 결과 메시지 전달하기
			dto = new MsgDTO();
			dto.setMsg(msg);

			log.info(this.getClass().getName() + ".noticeDelete End!");
		}

		return ResponseEntity.ok(dto);
	}
}
