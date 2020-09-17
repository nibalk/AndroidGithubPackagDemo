package com.nibalk.trafficcameramap.map.data.model

data class Camera(
    val camera_id: String,
    val image: String,
    val image_metadata: ImageMetadata,
    val location: Location,
    val timestamp: String
)

data class ImageMetadata(
    val height: Int,
    val md5: String,
    val width: Int
)

data class Location(
    val latitude: Double,
    val longitude: Double
)

data class Item(
    val cameras: List<Camera>,
    val timestamp: String
)