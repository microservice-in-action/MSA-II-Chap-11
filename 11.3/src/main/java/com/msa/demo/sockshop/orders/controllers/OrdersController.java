package com.msa.demo.sockshop.orders.controllers;

import com.msa.demo.sockshop.orders.entities.*;
import com.msa.demo.sockshop.orders.repositories.CustomerOrderRepository;
import com.msa.demo.sockshop.orders.resources.NewOrderResource;
import com.msa.demo.sockshop.orders.services.AsyncGetService;
import com.msa.demo.sockshop.orders.values.PaymentRequest;
import com.msa.demo.sockshop.orders.values.PaymentResponse;
import com.msa.demo.sockshop.shipping.entities.Shipment;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.apache.servicecomb.provider.springmvc.reference.RestTemplateBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.TypeReferences;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RepositoryRestController
@RestSchema(schemaId = "orders")
@RequestMapping(path = "/orders")
public class OrdersController {

	private final Logger LOG = LoggerFactory.getLogger(getClass());
	private static RestTemplate restTemplate;

	@Autowired
	private AsyncGetService asyncGetService;

	@Autowired
	private CustomerOrderRepository customerOrderRepository;

	@Value(value = "${http.timeout:5}")
	private long timeout;

	@RequestMapping(path = "customerId/{custId}", method = RequestMethod.GET)
	public List<CustomerOrder> findByCustomerId(@PathVariable("custId") String id)
			throws InterruptedException, IOException, ExecutionException, TimeoutException {
		List<CustomerOrder> orderList = customerOrderRepository.findByCustomerId(id);
		return orderList;
	}

	@RequestMapping(path = "{custId}/{username}", method = RequestMethod.GET)
	public boolean findShippingCharges(@PathVariable("custId") String id, @PathVariable("username") String uname)
			throws InterruptedException, IOException, ExecutionException, TimeoutException {
		String prefix = "cse://shipping";
		float amount = 0.0f;
		restTemplate = RestTemplateBuilder.create();
		Shipment shippment = restTemplate.postForObject(prefix + "/shipping/" + uname, new Shipment(id),
				Shipment.class);
		if (shippment.getAmount() == amount) {
			return false;
		}
		return true;
	}

	@RequestMapping(path = "/{orderId}", method = RequestMethod.GET)
	public CustomerOrder findById(@PathVariable("orderId") String id)
			throws InterruptedException, IOException, ExecutionException, TimeoutException {
		CustomerOrder orderList = customerOrderRepository.findById(id);
		return orderList;
	}

	@ResponseStatus(HttpStatus.CREATED)
	@RequestMapping(consumes = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public @ResponseBody CustomerOrder newOrder(@RequestBody NewOrderResource item) {
		try {
			if (item.address == null || item.items == null && item.customerId == null) {
				throw new InvalidOrderException(
						"Invalid order request. Order requires customer, address, card and items.");
			}

			LOG.info("Starting calls");
			LOG.info(item.address);
			LOG.info(item.items.toString());

			float amount = calculateTotal(item.items);

			LOG.info("amount :" + amount);

			PaymentRequest paymentRequest = new PaymentRequest(amount);
			restTemplate = RestTemplateBuilder.create();
			String prefixx = "cse://payment";
//			restTemplate.postForObject(prefixx + "/paymentAuth", paymentRequest, PaymentResponse.class);

			String customerId = item.customerId;
			String prefix = "cse://shipping";
//			Shipment shippment = restTemplate.postForObject(prefix + "/shipping/" + customerId, new Shipment(customerId),
//					Shipment.class);
//			Future<Shipment> shipmentFuture = new AsyncResult<>(shippment);
//			double shipmentFee = shipmentFuture.get(timeout, TimeUnit.SECONDS).getAmount();
			double shipmentFee = 0;
			CustomerOrder order = new CustomerOrder(null, customerId,
					new Address(item.address),
					item.items,
					null,
					Calendar.getInstance().getTime(),amount);

			LOG.info("Received data: " + order.toString());

			CustomerOrder savedOrder = customerOrderRepository.save(order);
			LOG.info("Saved order: " + savedOrder);

			return savedOrder;

		} catch (Exception e) {
			throw new IllegalStateException("Unable to create order due to timeout from one of the services.", e);
		}
	}

	private String parseId(String href) {

		Pattern idPattern = Pattern.compile("[\\w-]+$");

		Matcher matcher = idPattern.matcher(href);
		if (!matcher.find()) {
			throw new IllegalStateException("Could not parse user ID from: " + href);
		}

		return matcher.group(0);

	}

	private float calculateTotal(List<Item> items) {
		float amount = 0F;
		amount += items.stream().mapToDouble(i -> i.getQuantity() * i.getUnitPrice()).sum();
		return amount;
	}

	@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
	public class PaymentDeclinedException extends IllegalStateException {
		public PaymentDeclinedException(String s) {
			super(s);
		}
	}

	@ResponseStatus(value = HttpStatus.NOT_ACCEPTABLE)
	public class InvalidOrderException extends IllegalStateException {
		public InvalidOrderException(String s) {
			super(s);
		}
	}
}
