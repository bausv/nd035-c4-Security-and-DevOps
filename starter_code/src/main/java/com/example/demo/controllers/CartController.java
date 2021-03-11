package com.example.demo.controllers;

import java.util.Optional;
import java.util.function.IntConsumer;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MarkerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;

@RestController
@RequestMapping("/api/cart")
public class CartController {
	private static final Logger errorLogger = LoggerFactory.getLogger("errors");
	private static final Logger requestLogger = LoggerFactory.getLogger("requests");
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private CartRepository cartRepository;
	
	@Autowired
	private ItemRepository itemRepository;
	
	@PostMapping("/addToCart")
	@Transactional
	public ResponseEntity<Cart> addTocart(@RequestBody ModifyCartRequest request) {
		return getCartResponseEntity(request, CartAction.ADD, "addToCart", "add to cart request ");
	}

	@PostMapping("/removeFromCart")
	@Transactional
	public ResponseEntity<Cart> removeFromcart(@RequestBody ModifyCartRequest request) {
		return getCartResponseEntity(request, CartAction.REMOVE, "removeFromCart", "remove from cart request");
	}

	private ResponseEntity<Cart> getCartResponseEntity(ModifyCartRequest request, CartAction cartAction, String marker, String logPrefix) {
		User user = userRepository.findByUsername(request.getUsername());
		if(user == null) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Optional<Item> item = itemRepository.findById(request.getItemId());
		if(!item.isPresent()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
		}
		Cart cart = user.getCart();
		IntStream.range(0, request.getQuantity()).forEach(getCartAction(cartAction, item, cart));
		Cart saved = cartRepository.save(cart);
		requestLogger.info(MarkerFactory.getMarker(marker), logPrefix + request + " successfully processed, leading to cart " + saved);
		return ResponseEntity.ok(saved);
	}

	private IntConsumer getCartAction(CartAction cartAction, Optional<Item> item, Cart cart) {
		IntConsumer action = null;
		switch (cartAction) {
			case ADD:
				action = i -> cart.addItem(item.get());
				break;
			case REMOVE:
				action = i -> cart.removeItem(item.get());
				break;
		}
		return action;
	}

	enum CartAction {
		ADD, REMOVE;
	}
		
}
