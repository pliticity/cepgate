(function () {

    var common = angular.module('common');

    common.factory('Classification', ['$resource',
        function ($resource) {
            return $resource('/classification/:id', {}, {'query': {'url': '/classification', 'isArray': true}});
        }]);

    common.service('classificationService', ['$http','Classification', function ($http,Classification) {

        this.getAllForProduct = function(){
            return Classification.query({product:true});
        };

        this.getAll = function(active){
            return Classification.query({active:active,for:'0'});
        };

        this.getAllWithout = function(active,classificationId){
            return Classification.query({active:active,for:classificationId});
        };

        this.onClassificationSelected = function(classifications, classification){
            var selected = {};
            for(var i =0; i<classifications.length; i++){
                var type = classifications[i];
                if(type.id == classification.id){
                    selected = type;
                    break;
                }
            }
            classification.classificationId = selected.classificationId;
            classification.name = selected.name;
        };
    }]);

})();