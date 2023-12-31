package com.driver.controllers;

import com.driver.model.Customer;
import com.driver.model.TripBooking;
import com.driver.services.CustomerService;
import com.driver.services.impl.CustomerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customer")
public class CustomerController {
	CustomerService customerService=new CustomerServiceImpl();
	@PostMapping("/register")
	public ResponseEntity<Void> registerCustomer(@RequestBody Customer customer){
		customerService.register(customer);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	public void deleteCustomer(@RequestParam Integer customerId){
		customerService.deleteCustomer(customerId);
	}

	@PostMapping("/bookTrip")
	public ResponseEntity<Integer> bookTrip(@RequestParam Integer customerId, @RequestParam String fromLocation,
											@RequestParam String toLocation, @RequestParam Integer distanceInKm) throws Exception {
//		return new ResponseEntity<>(bookedTrip.getTripBookingId(), HttpStatus.CREATED);
		try{
			TripBooking tripBooking=customerService.bookTrip(customerId,fromLocation,toLocation,distanceInKm);
			return new ResponseEntity<Integer>(tripBooking.getTripBookingId(),HttpStatus.CREATED);
		}catch(Exception e){
			return null;
		}


	}

	@DeleteMapping("/complete")
	public void completeTrip(@RequestParam Integer tripId){
	}

	@DeleteMapping("/cancelTrip")
	public void cancelTrip(@RequestParam Integer tripId){
	}
}
