package com.daimler.configuratorbackend.exception

class ModelNotFoundException(searchParam: String) : RuntimeException("Could not find any vehicle model for search parameter: " + searchParam)
