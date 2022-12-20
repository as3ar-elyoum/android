package com.as3arelyoum.domain.usecase.user

import com.as3arelyoum.data.repository.AssarRepository
import com.as3arelyoum.domain.mapper.user.toUserInfoDTO
import com.as3arelyoum.domain.mapper.user.toUserInfoModel
import com.as3arelyoum.domain.model.user.UserInfoModel
import javax.inject.Inject

class SendDeviceIdUseCase @Inject constructor(private val repository: AssarRepository) {
    suspend operator fun invoke(userInfoModel: UserInfoModel, deviceId: String): UserInfoModel {
        return repository.sendDeviceId(userInfoModel.toUserInfoDTO(), deviceId).toUserInfoModel()
    }
}