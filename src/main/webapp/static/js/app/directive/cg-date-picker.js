(function () {
    var common = angular.module('common');

    common.directive('cgDatePicker', [function () {
        return {
            require: 'ngModel',
            link: function (scope, el, attr, ngModel) {
                $(el).datepicker({
                    format: 'dd/mm/yyyy'
                }).on('changeDate', function (ev) {
                    scope.$apply(function () {
                        ngModel.$setViewValue(ev.currentTarget.value);
                    });
                });;
            }
        };
    }])
})();