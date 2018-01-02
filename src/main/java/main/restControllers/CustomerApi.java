package main.restControllers;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import main.entities.repoInterfaces.CouponRepo;
import main.entities.services.CustomerService;
import main.entities.tables.Coupon;
import main.entities.tables.Customer;
import main.entities.tables.enums.CouponType;
import webComponents.WebCoupon;

@RestController
@RequestMapping(value = "customer")
public class CustomerApi {
	
	@Autowired
	CustomerService customerService;
	@Autowired
	CouponRepo couponRepo;
	
	private Customer getFacade(HttpServletRequest request, HttpServletResponse response) {
		HttpSession httpSession = request.getSession();
		
		// Get the company from the session
		int cust_id = (int) httpSession.getAttribute("id");
		return customerService.findCustomerById(cust_id);
	}
	
	@RequestMapping(value = "coupon/{id}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<WebCoupon> getCouponById(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") int id) {
		
		Customer customer = getFacade(request, response);
		if (customer == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		
		Coupon coupon = couponRepo.findOne(id);
		if (coupon != null) {
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
					.body(new WebCoupon(coupon));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
		
	}
	
	@RequestMapping(value = "coupon/{id}", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Object> purchaseCoupon(
			HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") int id) {
		
		Customer customer = getFacade(request, response);
		if (customer == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		
		List<Integer> couponIds = new ArrayList<>();
		couponIds.add(id);
		customerService.purchaseCoupons(couponIds, customer.getId());
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
				.body("");
	}
	
	@RequestMapping(value = "coupons", method = RequestMethod.POST)
	public @ResponseBody ResponseEntity<Object> purchaseCoupons(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestBody List<Integer> couponIds) {
		
		Customer customer = getFacade(request, response);
		if (customer == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		
		customerService.purchaseCoupons(couponIds, customer.getId());
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON)
				.body("");
	}
	
	//
	// @formatter:off
/*	#######################
 *   get coupons methods
 * 	####################### */
	// @formatter:on
	
	@RequestMapping(value = "coupon/all", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<WebCoupon>> getCouponsByType(
			HttpServletRequest request, HttpServletResponse response) {
		
		Customer customer = getFacade(request, response);
		if (customer == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		
		List<Coupon> coupons = customerService.getAllCoupons(customer);
		List<WebCoupon> webCoupons = new ArrayList<>();
		for (Coupon coupon : coupons) {
			webCoupons.add(new WebCoupon(coupon));
		}
		
		if (0 < webCoupons.size()) {
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(webCoupons);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
	}
	
	@RequestMapping(value = "coupon/type/{type}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<WebCoupon>> getCouponsByType(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("type") CouponType type) {
		
		Customer customer = getFacade(request, response);
		if (customer == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		
		List<Coupon> coupons = customerService.getCouponsByType(customer, type);
		List<WebCoupon> webCoupons = new ArrayList<>();
		for (Coupon coupon : coupons) {
			webCoupons.add(new WebCoupon(coupon));
		}
		
		if (0 < webCoupons.size()) {
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(webCoupons);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
	}
	
	@RequestMapping(value = "coupon/price/{price}", method = RequestMethod.GET)
	public @ResponseBody ResponseEntity<List<WebCoupon>> getAllCouponsbyPrice(
			HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("price") double price) {
		
		Customer customer = getFacade(request, response);
		if (customer == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
		
		List<Coupon> coupons = customerService.getCouponsByPrice(customer, price);
		List<WebCoupon> webCoupons = new ArrayList<>();
		for (Coupon coupon : coupons) {
			webCoupons.add(new WebCoupon(coupon));
		}
		
		if (0 < webCoupons.size()) {
			return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(webCoupons);
		} else {
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
		}
	}
	
}