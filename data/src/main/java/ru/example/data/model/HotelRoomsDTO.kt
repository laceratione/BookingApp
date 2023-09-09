package ru.example.data.model

import com.google.gson.annotations.SerializedName

class HotelRoomsDTO(
    @SerializedName("rooms") val rooms: List<HotelRoomDTO>
)

fun HotelRoomsDTO.mapToDomain() = rooms.map { roomDTO -> roomDTO.mapToDomain() }