package com.taeyeon.xoduslol.util


@JsFun("str => encodeURIComponent(str)")
external fun encodeURIComponent(str: String): String

@JsFun("str => decodeURIComponent(str)")
external fun decodeURIComponent(str: String): String
