package com.greenwaymyanmar.core.domain.usecase

import greenway_myanmar.org.features.fishfarmrecord.domain.model.ValidationResult

abstract class ValidationUseCase<in P, out R> {
    operator fun invoke(params: P): ValidationResult<R> {
        return execute(params)
    }

    protected abstract fun execute(params: P): ValidationResult<R>
}