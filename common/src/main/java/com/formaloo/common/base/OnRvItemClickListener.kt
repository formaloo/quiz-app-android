package com.formaloo.common.base

interface OnRvItemClickListener<T> {
    fun onItemClick(item: T, position: Int)
}