package com.sample.api.service;

import com.sample.api.dto.SampleDto;
import com.sample.api.dto.SampleDto.LowestPriceResponse;
import com.sample.api.dto.SampleDto.CheapestComboResponse;


public interface SampleService {
    CheapestComboResponse getCheapestCombo();

    LowestPriceResponse getLowestPrice();

    SampleDto.PriceRangeResponse getPriceRange(String category);
}
