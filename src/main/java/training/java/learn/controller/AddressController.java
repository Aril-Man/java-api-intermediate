package training.java.learn.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import training.java.learn.dto.AddressResponse;
import training.java.learn.dto.CreateAddressRequest;
import training.java.learn.dto.WebResponse;
import training.java.learn.entity.User;
import training.java.learn.service.AddressService;

@RestController
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping(
            path = "/api/address/create",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> create(User user, @RequestBody CreateAddressRequest request) {
        AddressResponse result = addressService.create(user, request);

        return WebResponse.<AddressResponse>builder()
                .data(result)
                .build();
    }

    @GetMapping(
            path = "/api/address/current",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public WebResponse<AddressResponse> getCurrentAddress(User user) {
        AddressResponse result = addressService.getCurrentAddress(user);

        return WebResponse.<AddressResponse>builder().data(result).build();
    }
}
