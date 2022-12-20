package com.as3arelyoum.domain.mapper.user

import com.as3arelyoum.data.remote.response.dto.UserInfoDTO
import com.as3arelyoum.domain.model.user.UserInfoModel

fun UserInfoDTO.toUserInfoModel(): UserInfoModel {
    return UserInfoModel(
        device_id = device_id,
        fcm_token = fcm_token
    )
}

fun UserInfoModel.toUserInfoDTO(): UserInfoDTO {
    return UserInfoDTO(
        device_id = device_id,
        fcm_token = fcm_token
    )
}