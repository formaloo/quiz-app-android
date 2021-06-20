package co.idearun.learningapp.common.exception


class ViewFailure {
    class responseError(msg: String?) : Failure.FeatureFailure(msg)
}
