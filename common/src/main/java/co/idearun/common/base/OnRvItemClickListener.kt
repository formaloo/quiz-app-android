package co.idearun.common.base

interface OnRvItemClickListener<T> {
    fun onItemClick(item: T, position: Int)
}