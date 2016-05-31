(function () {

    var dhd = angular.module('dhd');

    dhd.directive("formError", ['$compile', function ($compile) {
        return {
            compile: function (element, attrs) {

                element.removeAttr('form-error');

                var formId = attrs.form;
                var fieldId = formId + "." + attrs.field;
                var errors = JSON.parse(attrs.messages);

                var ngClass = "{ 'has-error' : " + fieldId + ".$invalid && (!" + fieldId + ".$pristine || "+formId+".$submitted) }";

                var messages = "<div class='row' ng-show='(" + formId + ".$submitted || !" + fieldId + ".$pristine) && " + fieldId + ".$invalid' ng-messages='" + fieldId + ".$error'> <div class='col-xs-12 text-right' role='alert'>";

                for (var key in errors) {
                    messages += "<p ng-message='"+key+"' class='help-block'>"+errors[key]+"</p>";
                }

                messages += "</div></div>";


                element.attr('ng-class', ngClass);
                element.append(messages);

                var fn = $compile(element);
                return function(scope){
                    fn(scope);
                };

            }
        }


    }]);

})();