package com.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dto.CartDTO;
import com.dto.GoodsDTO;
import com.dto.MemberDTO;
import com.dto.OrderDTO;
import com.service.GoodsService;
import com.service.MemberService;

@Controller
public class GoodsController {

	@Autowired
	GoodsService service;
	@Autowired
	MemberService mService;

	@RequestMapping("/loginCheck/orderDone")
	public String orderDone(OrderDTO oDTO, int orderNum, HttpSession session, RedirectAttributes xxx) {
		System.out.println(oDTO + "\t" + orderNum);
		MemberDTO dto = (MemberDTO) session.getAttribute("login");

		oDTO.setUserid(dto.getUserid());
		service.orderDone(oDTO, orderNum);

		xxx.addFlashAttribute("oDTO", oDTO);
		return "redirect:../orderDone";
	}

	@RequestMapping("/loginCheck/orderConfirm")
	public String orderConfirm(@RequestParam("num") int num, HttpSession session, RedirectAttributes xxx) {
		MemberDTO mDTO = (MemberDTO) session.getAttribute("login");
		String userid = mDTO.getUserid();
		mDTO = mService.myPage(userid);
		CartDTO cart = service.orderConfirmByNum(num);
		xxx.addFlashAttribute("mDTO", mDTO);
		xxx.addFlashAttribute("cDTO", cart);
		return "redirect:../orderConfirm";
	}

	@RequestMapping(value = "/loginCheck/delAllCart")
	public String delAllCart(@RequestParam("check") ArrayList<String> list) {
		System.out.println(list);
		service.delAllCart(list);
		return "redirect:../loginCheck/cartList";
	}

	@RequestMapping(value = "/loginCheck/cartDelete")
	@ResponseBody
	public void cartDelete(@RequestParam("num") int num) {
		System.out.println(num);
		service.cartDelete(num);
	}

	@RequestMapping(value = "/loginCheck/cartUpdate")
	@ResponseBody
	public void cartUpdate(@RequestParam Map<String, String> map) {
		System.out.println(map);
		service.cartUpdate(map);
	}

	@RequestMapping("/loginCheck/cartList")
	public String cartList(RedirectAttributes attr, HttpSession session) {
		MemberDTO dto = (MemberDTO) session.getAttribute("login");
		String userid = dto.getUserid();
		List<CartDTO> list = service.cartList(userid);
		attr.addFlashAttribute("cartList", list);
		return "redirect:../cartList";
	}

	@RequestMapping("/loginCheck/cartAdd")
	public String cartAdd(CartDTO cart, HttpSession session) {
		MemberDTO mDTO = (MemberDTO) session.getAttribute("login");
		cart.setUserid(mDTO.getUserid());
		session.setAttribute("mesg", cart.getgCode());
		service.cartAdd(cart);
		return "redirect:../goodsRetrieve?gCode=" + cart.getgCode();
	}

	@RequestMapping("/goodsRetrieve")
	@ModelAttribute("goodsRetrieve")
	public GoodsDTO goodsRetrieve(@RequestParam("gCode") String gCode) {
		GoodsDTO dto = service.goodsRetrieve(gCode);
		return dto;
	}

	@RequestMapping("/goodsList")
	public ModelAndView goodsList(@RequestParam("gCategory") String gCategory) {
		if (gCategory == null) {
			gCategory = "top";
		}
		List<GoodsDTO> list = service.goodsList(gCategory);
		ModelAndView mav = new ModelAndView();
		mav.addObject("goodsList", list);
		mav.setViewName("main");
		return mav;
	}

}
