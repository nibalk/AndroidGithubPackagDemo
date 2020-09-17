package com.nibalk.framework.base.utils

class BaseException(
    private var errorCode: String? = null,
    private var throwable: Throwable? = null,
    var errorTitle: String? = null,
    var errorMessage: String? = null
) : RuntimeException()