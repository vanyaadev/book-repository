package com.vanyaadev.bookrepository.service;

import com.vanyaadev.bookrepository.exception.ResourceAlreadyExistsException;
import com.vanyaadev.bookrepository.exception.ResourceNotFoundException;
import com.vanyaadev.bookrepository.model.Firm;
import com.vanyaadev.bookrepository.repository.FirmRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FirmService {

    private final FirmRepository firmRepository;

    @Autowired
    public FirmService(FirmRepository firmRepository) {
        this.firmRepository = firmRepository;
    }

    public Firm getFirmByNameAndCity(String name, String city) {

        return returnFirmIfExistsByNameAndCity(name, city);
    }


    public Firm createFirm(Firm firm) {
        if (firmRepository
                .findByNameAndCity(firm.getName(), firm.getCity())
                .isPresent())
            throw new ResourceAlreadyExistsException("Firm already exists!");
        else
            return firmRepository.save(firm);
    }


    Firm returnFirmIfExistsByNameAndCity(String name, String city) {
        if (firmRepository.findByNameAndCity(name, city).isEmpty()) {
            throw new ResourceNotFoundException("Publisher with name: {" + name + "} and city: {" + city + "} not found!");
        } else
            return firmRepository.findByNameAndCity(name, city).get();
    }


}