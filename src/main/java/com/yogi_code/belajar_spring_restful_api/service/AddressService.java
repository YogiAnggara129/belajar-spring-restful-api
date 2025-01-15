package com.yogi_code.belajar_spring_restful_api.service;

import com.yogi_code.belajar_spring_restful_api.entity.Address;
import com.yogi_code.belajar_spring_restful_api.entity.Contact;
import com.yogi_code.belajar_spring_restful_api.entity.User;
import com.yogi_code.belajar_spring_restful_api.model.AddressResponse;
import com.yogi_code.belajar_spring_restful_api.model.CreateAddressRequest;
import com.yogi_code.belajar_spring_restful_api.model.UpdateAddressRequest;
import com.yogi_code.belajar_spring_restful_api.repository.AddressRepository;
import com.yogi_code.belajar_spring_restful_api.repository.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
public class AddressService {
    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ValidationService validationService;

    @Transactional
    public AddressResponse create(User user, CreateAddressRequest request) {
        validationService.validate(request);

        final Contact contact = getContact(user, request.getContactId());

        final Address address = new Address();
        address.setId(UUID.randomUUID().toString());
        address.setContact(contact);
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());

        addressRepository.save(address);
        return toAddressResponse(address);
    }

    @Transactional(readOnly = true)
    public AddressResponse get(User user, String contactId, String addressId) {
        final Contact contact = getContact(user, contactId);
        final Address address = getAddress(contact, addressId);
        return toAddressResponse(address);
    }

    @Transactional
    public AddressResponse update(User user, UpdateAddressRequest request) {
        validationService.validate(request);

        final Contact contact = getContact(user, request.getContactId());
        final Address address = getAddress(contact, request.getAddressId());

        address.setId(request.getAddressId());
        address.setContact(contact);
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setProvince(request.getProvince());
        address.setPostalCode(request.getPostalCode());
        address.setCountry(request.getCountry());

        addressRepository.save(address);
        return toAddressResponse(address);
    }

    @Transactional
    public void remove(User user, String contactId, String addressId) {
        final Contact contact = getContact(user, contactId);
        final Address address = getAddress(contact, addressId);
        addressRepository.delete(address);
    }

    @Transactional(readOnly = true)
    public List<AddressResponse> list(User user, String contactId) {
        final Contact contact = getContact(user, contactId);
        final List<Address> addresses = addressRepository.findAllByContact(contact);
        return addresses.stream().map(this::toAddressResponse).collect(toList());
    }

    private Contact getContact(User user, String contactId) {
        return contactRepository.findFirstByUserAndId(user, contactId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact is not found"));
    }

    private Address getAddress(Contact contact, String addressId) {
        return addressRepository.findFirstByContactAndId(contact, addressId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address is not found"));
    }

    private AddressResponse toAddressResponse(Address address) {
        return AddressResponse.builder()
               .id(address.getId())
               .street(address.getStreet())
               .city(address.getCity())
               .province(address.getProvince())
               .postalCode(address.getPostalCode())
               .country(address.getCountry())
               .build();
    }
}
