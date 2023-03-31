package com.switchfully.order.api.order;

import com.switchfully.order.domain.models.order.Order;
import com.switchfully.order.domain.models.user.Customer;
import com.switchfully.order.service.order.OrderService;
import com.switchfully.order.service.support.dto.order.OrderDTO;
import com.switchfully.order.service.support.dto.order.OrderReportDTO;
import com.switchfully.order.service.support.dto.user.CustomerDTO;
import com.switchfully.order.service.support.wrapper.OrderDTOWrapper;
import com.switchfully.order.service.support.wrapper.OrderWrapper;
import com.switchfully.order.service.user.SecurityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static com.switchfully.order.domain.models.user.Feature.*;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping(value = "orders")
public class OrderController {
    private final OrderService orderService;
    private final SecurityService securityService;
    private final Logger myLogger = LoggerFactory.getLogger(ItemController.class);

    @Autowired
    public OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @ResponseStatus(CREATED)
    @PostMapping(consumes = "application/json", produces = "application/json")
    public OrderDTOWrapper createOrder(@RequestBody OrderWrapper orderWrapper,
                                       @RequestHeader(required = false) String authorization)
            throws AuthenticationException {
        myLogger.info("Adding a New Order to the Database.");
        securityService.validateUser(authorization, CAN_ORDER_ITEMS);
        securityService.authenticateUser(orderWrapper.getCustomerId(), authorization);
        return orderService.createOrder(orderWrapper);
    }

    @ResponseStatus(OK)
    @GetMapping(produces = "application/json")
    public OrderReportDTO getOrdersByCustomer(@RequestParam(required = false) String customerId,
                                              @RequestHeader(required = false) String authorization)
            throws AuthenticationException {
        myLogger.info("Retrieving all the orders from customer " + customerId);
        securityService.validateUser(authorization, CAN_RETRIEVE_ORDERS);
        securityService.authenticateUser(customerId, authorization);
        return orderService.getOrdersByCustomer(customerId);
    }

    @ResponseStatus(OK)
    @GetMapping(produces = "application/json", value = "shippingTomorrow")
    public Map<CustomerDTO, List<OrderDTO>> getOrderByShippingDate() {
        return orderService.getOrderByShippingDate(LocalDate.now().plusDays(1));
    }
}
