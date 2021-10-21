package com.infolab.recyclingstarter.model

data class User(
        val id: Int?,
        var first_name: String?,
        val building: Int?,
        val email: String?,
        var phone: String?,
        var room: String?,
        val organization: Int?,
        val boxes: MutableList<Box>?,
        val is_active: Boolean?
)
