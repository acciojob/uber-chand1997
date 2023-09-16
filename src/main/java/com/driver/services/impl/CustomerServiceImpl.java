package com.driver.services.impl;

import com.driver.exceptions.CustomerNotFoundException;
import com.driver.exceptions.NoCabAvailableException;
import com.driver.exceptions.TripBookingNotAvailableException;
import com.driver.model.TripBooking;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.model.Customer;
import com.driver.model.Driver;
import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;
import com.driver.model.TripStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		customerRepository2.deleteById(customerId);

	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception{
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception
		//Avoid using SQL query
		TripBooking tripBooking=new TripBooking();
		tripBooking.setFromLocation(fromLocation);
		tripBooking.setToLocation(toLocation);
		tripBooking.setDistanceInKm(distanceInKm);

		Optional<Customer> optionalCustomer=customerRepository2.findById(customerId);
		if(!optionalCustomer.isPresent()){
			throw new CustomerNotFoundException("Invalid Customer-Id");
		}

			Customer customer=optionalCustomer.get();
			tripBooking.setCustomer(customer);

		List<Driver> driverList=driverRepository2.findAll();
		if(driverList.isEmpty()) throw new NoCabAvailableException("No Driver Registered");

		int minId=Integer.MAX_VALUE;
		for(Driver d:driverList){
			if(d.getCab().isAvailable())
			minId=Math.min(minId,d.getDriverId());
		}

		Optional<Driver> optionalDriver=driverRepository2.findById(minId);
		Driver driver=optionalDriver.get();

		tripBooking.setDriver(driver);
		driver.getCab().setAvailable(false);


		tripBooking.setTripStatus(TripStatus.CONFIRMED);


		tripBooking.setBill(distanceInKm*driver.getCab().getPerKmRate());

		TripBooking savedTripBooking=tripBookingRepository2.save(tripBooking);

		customer.getTripBookingList().add(savedTripBooking);
		driver.getTripBookingList().add(savedTripBooking);




		return savedTripBooking;

	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		Optional<TripBooking> optionalTripBooking=tripBookingRepository2.findById(tripId);
		if(!optionalTripBooking.isPresent()){
			throw new TripBookingNotAvailableException("Invalid TripBooking-Id");
		}
		TripBooking tripBooking=optionalTripBooking.get();
		tripBookingRepository2.delete(tripBooking);
		tripBooking.setTripStatus(TripStatus.CANCELED);
		tripBooking.getDriver().getCab().setAvailable(true);

		Driver driver=tripBooking.getDriver();

		Customer customer=tripBooking.getCustomer();

		TripBooking savedTripBooking=tripBookingRepository2.save(tripBooking);
		driver.getTripBookingList().add(savedTripBooking);
		customer.getTripBookingList().add(savedTripBooking);

		driverRepository2.save(driver);
		customerRepository2.save(customer);
	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly

		Optional<TripBooking> optionalTripBooking=tripBookingRepository2.findById(tripId);
		if(!optionalTripBooking.isPresent()){
			throw new TripBookingNotAvailableException("Invalid TripBooking-Id");
		}
		TripBooking tripBooking=optionalTripBooking.get();
		tripBookingRepository2.delete(tripBooking);
		tripBooking.setTripStatus(TripStatus.COMPLETED);
		tripBooking.getDriver().getCab().setAvailable(true);

		Driver driver=tripBooking.getDriver();

		Customer customer=tripBooking.getCustomer();

		TripBooking savedTripBooking=tripBookingRepository2.save(tripBooking);
		driver.getTripBookingList().add(savedTripBooking);
		customer.getTripBookingList().add(savedTripBooking);

		driverRepository2.save(driver);
		customerRepository2.save(customer);

	}
}
