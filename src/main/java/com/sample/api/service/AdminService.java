package com.sample.api.service;

import com.sample.api.dto.AdminDto.*;


public interface AdminService {

    CreateResponseDto create(CreateRequestDto request);

    UpdateResponseDto update(UpdateRequestDto request);

    String delete(DeleteRequestDto request);
}
